package com.kappstats.domain.core

import org.jetbrains.compose.resources.StringResource

sealed class Resource<T, R> {

    data class Failure<T, R>(val type: R, val message: StringResource? = null) : Resource<T, R>()
    data class Success<T, R>(val data: T? = null) : Resource<T, R>()

    val isSuccess: Boolean
        get() = this is Success

    val asDataOrNull: T?
        get() = if (this is Success) data else null

    val asMessageOrNull: StringResource?
        get() = if (this is Failure) message else null

}