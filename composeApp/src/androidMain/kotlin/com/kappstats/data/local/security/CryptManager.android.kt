package com.kappstats.data.local.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


class AndroidCryptManagerImpl : CryptManager {
    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "KAppStatsKey"
        private const val AES_ALGORITHM = "AES/GCM/NoPadding"
        private const val IV_SIZE = 12
        private const val TAG_SIZE = 128
    }

    init {
        generateKeyIfNotExist()
    }

    private fun generateKeyIfNotExist() {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            val spec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
            keyGenerator.init(spec)
            keyGenerator.generateKey()
        }
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    override fun encrypt(text: String): String {
        val cipher = Cipher.getInstance(AES_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(text.encodeToByteArray())
        val result = iv + encrypted
        return Base64.encodeToString(result, Base64.NO_WRAP)
    }

    override fun decrypt(encryptedText: String): String? = runCatching {
        val decoded = Base64.decode(encryptedText, Base64.NO_WRAP)
        val iv = decoded.copyOfRange(0, IV_SIZE)
        val encryptedData = decoded.copyOfRange(IV_SIZE, decoded.size)
        val cipher = Cipher.getInstance(AES_ALGORITHM)
        val spec = GCMParameterSpec(TAG_SIZE, iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
        val decrypted = cipher.doFinal(encryptedData)
        String(decrypted, Charsets.UTF_8)
    }.getOrNull()
}

actual fun getCryptManager(): CryptManager = AndroidCryptManagerImpl()