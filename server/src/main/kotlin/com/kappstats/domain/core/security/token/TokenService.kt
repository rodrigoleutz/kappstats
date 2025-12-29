package com.kappstats.domain.core.security.token

interface TokenService {
    fun decode(token: String): Map<String, String>?
    fun generate(
        vararg claims: TokenClaim
    ): String
}
