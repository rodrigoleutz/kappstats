package com.kappstats.data.local.api

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.storage.storeOf
import kotlinx.serialization.Serializable

actual inline fun <reified T : @Serializable Any> getKStore(): LocalKStoreApi<T> {
    return object : LocalKStoreApi<T> {
        override val store: KStore<T> = storeOf(key = "${T::class.simpleName}")
    }
}