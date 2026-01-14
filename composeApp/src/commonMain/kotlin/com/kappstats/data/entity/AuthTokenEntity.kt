package com.kappstats.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokenEntity(
    val token: String
)