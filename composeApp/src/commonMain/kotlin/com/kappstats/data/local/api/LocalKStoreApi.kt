package com.kappstats.data.local.api

import io.github.xxfast.kstore.KStore
import kotlinx.serialization.Serializable

interface LocalKStoreApi<T : @Serializable Any> {
    val store: KStore<T>
}

expect inline fun <reified T: @Serializable Any> getKStore(): LocalKStoreApi<T>