package com.kappstats.domain.core

import org.jetbrains.compose.resources.StringResource

sealed class Resource<T> {

    data class Failure<T>(val message: StringResource) : Resource<T>()
    data class Success<T>(val data: T? = null) : Resource<T>()

    val isSuccess: Boolean
        get() = this is Success

    val asDataOrNull: T?
        get() = if (this is Success) data else null

    val asMessageOrNull: StringResource?
        get() = if (this is Failure) message else null

}