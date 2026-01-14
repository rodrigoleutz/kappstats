package com.kappstats.data.local.security

import kotlinx.browser.window
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.w3c.dom.get
import org.w3c.dom.set


external interface Crypto {
    fun getRandomValues(array: Uint8Array)
}

@OptIn(ExperimentalWasmJsInterop::class)
private val crypto: Crypto = js("window.crypto")
class WasmJsCryptManagerImpl : CryptManager {
    private val storageKey = "kappstats_v_key"

    private fun getSeed(): String {
        return window.localStorage[storageKey] ?: run {
            val array = Uint8Array(32)
            crypto.getRandomValues(array)
            val newSeed = (0 until array.length).joinToString("") { i ->
                array[i].toString(16).padStart(2, '0')
            }
            window.localStorage[storageKey] = newSeed
            newSeed
        }
    }

    override fun encrypt(text: String): String {
        val seed = getSeed()
        val xorEd = text.mapIndexed { i, c ->
            (c.code xor seed[i % seed.length].code).toChar()
        }.joinToString("")
        return window.btoa(xorEd)
    }

    override fun decrypt(encryptedText: String): String? = runCatching {
        val seed = getSeed()
        val decoded = window.atob(encryptedText)
        decoded.mapIndexed { i, c ->
            (c.code xor seed[i % seed.length].code).toChar()
        }.joinToString("")
    }.getOrNull()
}

actual fun getCryptManager(): CryptManager = WasmJsCryptManagerImpl()