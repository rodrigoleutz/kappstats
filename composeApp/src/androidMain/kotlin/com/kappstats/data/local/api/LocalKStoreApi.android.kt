package com.kappstats.data.local.api

import com.kappstats.util.AndroidContextProvider
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.Serializable

actual inline fun <reified T : @Serializable Any> getKStore(): LocalKStoreApi<T> {
    val context = AndroidContextProvider.get()
    val filesDir = context.filesDir.absolutePath
    val fileName = "${T::class.simpleName}.bin"
    if (!SystemFileSystem.exists(Path(filesDir))) {
        SystemFileSystem.createDirectories(Path(filesDir))
    }
    return object : LocalKStoreApi<T> {
        override val store: KStore<T> = storeOf(file = Path("$filesDir/$fileName"))
    }
}