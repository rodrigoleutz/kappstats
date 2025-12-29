package com.kappstats.domain.core.resource

import io.ktor.http.HttpStatusCode


sealed class Resource<T> {

    data class Failure<T>(val status: HttpStatusCode, val message: String? = null): Resource<T>()
    data class Success<T>(val data: T, val status: HttpStatusCode = HttpStatusCode.OK): Resource<T>()

    val asDataOrNull: T?
        get() = when(this) {
            is Failure -> null
            is Success -> data
        }

    val isSuccess: Boolean
        get() = this is Success<*>

    val statusCode: HttpStatusCode
        get() = when(this) {
            is Failure -> status
            is Success<*> -> status
        }
}