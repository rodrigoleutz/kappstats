package com.kappstats.domain.core.security.token

import com.auth0.jwt.JWTVerifier
import io.ktor.server.auth.jwt.JWTCredential

interface TokenService {
    val config: TokenConfig
    fun decode(token: String): Map<String, String>?
    fun generate(
        vararg claims: TokenClaim
    ): String
    fun validate(credential: JWTCredential): Any?
    fun verifier(): JWTVerifier
}
