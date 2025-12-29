package com.kappstats.domain.core.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

class JwtTokenServiceImpl(private val config: TokenConfig) : TokenService {

    override fun decode(token: String): Map<String, String>? {
        return try {
            val decodedJWT = JWT.require(Algorithm.HMAC256(config.secret))
                .withIssuer(config.issuer)
                .withAudience(config.audience)
                .build()
                .verify(token)
            decodedJWT.claims.mapValues { (_, claim) ->
                claim.asString() ?: claim.asInt()?.toString()
                ?: claim.asBoolean()?.toString()
                ?: claim.asDate()?.toString()
                ?: "null"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun generate(vararg claims: TokenClaim): String {
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))
        claims.forEach { claim ->
            token = token.withClaim(claim.name, claim.value)
        }
        return token.sign(Algorithm.HMAC256(config.secret))
    }
}
