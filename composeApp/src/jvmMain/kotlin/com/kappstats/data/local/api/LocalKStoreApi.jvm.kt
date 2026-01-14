package com.kappstats.data.local.api

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.Serializable
import net.harawata.appdirs.AppDirsFactory



actual inline fun <reified T : @Serializable Any> getKStore(): LocalKStoreApi<T> {
    val PACKAGE_NAME = "com.kappstats"
    val VERSION = "1.0"
    val ORGANISATION = "kappstats"
    val filesDir = AppDirsFactory.getInstance().getUserDataDir(PACKAGE_NAME, VERSION, ORGANISATION)
    with(SystemFileSystem) { if(!exists(Path(filesDir))) createDirectories(Path(filesDir)) }
    val file = T::class.simpleName.toString()+".bin"
    return object : LocalKStoreApi<T> {
        override val store: KStore<T> = storeOf(file = Path("${filesDir}/$file"))
    }
}