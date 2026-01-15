package com.kappstats.data.local.api

import com.kappstats.util.getLocalAppPath
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import kotlinx.serialization.Serializable


actual inline fun <reified T : @Serializable Any> getKStore(): LocalKStoreApi<T> {
    val filesDir = getLocalAppPath()
    val file = T::class.simpleName.toString()+".bin"
    return object : LocalKStoreApi<T> {
        override val store: KStore<T> = storeOf(file = Path("${filesDir}/$file"))
    }
}