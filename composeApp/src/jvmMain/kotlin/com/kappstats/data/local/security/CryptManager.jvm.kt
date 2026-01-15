package com.kappstats.data.local.security

import com.kappstats.util.getLocalAppPath
import java.io.File
import java.security.KeyStore
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class JvmCryptManagerImpl(
    private val keyStoreFile: File,
    private val password: CharArray
) : CryptManager {

    companion object {
        private const val ALGORITHM = "AES/GCM/NoPadding"
        private const val KEY_ALIAS = "DesktopKAppStatsKey"
        private const val IV_SIZE = 12
        private const val TAG_SIZE = 128
    }

    private val keyStore: KeyStore = KeyStore.getInstance("PKCS12")

    init {
        if (keyStoreFile.exists()) {
            keyStore.load(keyStoreFile.inputStream(), password)
        } else {
            keyStore.load(null, password)
            generateAndSaveKey()
        }
    }

    private fun generateAndSaveKey() {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        val key = keyGenerator.generateKey()
        val entry = KeyStore.SecretKeyEntry(key)
        val protection = KeyStore.PasswordProtection(password)
        keyStore.setEntry(KEY_ALIAS, entry, protection)
        keyStoreFile.outputStream().use { keyStore.store(it, password) }
    }

    private fun getSecretKey(): SecretKey {
        val protection = KeyStore.PasswordProtection(password)
        val entry = keyStore.getEntry(KEY_ALIAS, protection) as KeyStore.SecretKeyEntry
        return entry.secretKey
    }

    override fun encrypt(text: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(text.encodeToByteArray())
        val result = iv + encrypted
        return Base64.getEncoder().encodeToString(result)
    }

    override fun decrypt(encryptedText: String): String? = runCatching {
        val decoded = Base64.getDecoder().decode(encryptedText)
        val iv = decoded.copyOfRange(0, IV_SIZE)
        val encryptedData = decoded.copyOfRange(IV_SIZE, decoded.size)
        val cipher = Cipher.getInstance(ALGORITHM)
        val spec = GCMParameterSpec(TAG_SIZE, iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
        String(cipher.doFinal(encryptedData), Charsets.UTF_8)
    }.getOrNull()
}

private fun getHardwareIdFromSystem(): String {
    val os = System.getProperty("os.name")
    val user = System.getProperty("user.name")
    val arch = System.getProperty("os.arch")
    return "$os-$user-$arch-KAppStats"
}

private fun getHardwareId(): CharArray {
    val os = System.getProperty("os.name").lowercase()
    val command = when {
        os.contains("win") -> "wmic csproduct get uuid"
        os.contains("mac") -> "system_profiler SPHardwareDataType"
        else -> "cat /etc/machine-id"
    }
    return try {
        val process = Runtime.getRuntime().exec(command)
        val output = process.inputStream.bufferedReader().use { it.readText() }
        val id = output.replace("UUID", "", ignoreCase = true).filter { it.isLetterOrDigit() }
        if (id.isBlank()) getHardwareIdFromSystem().toCharArray()
        else id.toCharArray()
    } catch (e: Exception) {
        getHardwareIdFromSystem().toCharArray()
    }
}

actual fun getCryptManager(): CryptManager {
    val filesDir = getLocalAppPath()
    val keyStoreFile = File(filesDir, ".crypt/keystore.p12")
    if (keyStoreFile.parentFile?.exists() == false) {
        keyStoreFile.parentFile?.mkdirs()
    }
    return JvmCryptManagerImpl(keyStoreFile, getHardwareId())
}