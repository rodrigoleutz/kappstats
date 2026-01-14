package com.kappstats.data.local.security

interface CryptManager {
    fun decrypt(encryptedText: String): String?
    fun encrypt(text: String): String
}

expect fun getCryptManager(): CryptManager