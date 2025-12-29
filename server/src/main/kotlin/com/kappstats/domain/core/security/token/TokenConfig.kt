package com.kappstats.domain.core.security.token

data class TokenConfig(
    val issuer: String,
    val audience: String,
    val realm: String,
    val expiresIn: Long = 365L * 1000L * 60L * 60L * 24L,
    val secret: String
)