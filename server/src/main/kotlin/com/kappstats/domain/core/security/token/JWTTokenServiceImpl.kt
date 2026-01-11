package com.kappstats.domain.core.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.kappstats.data.repository.user.AuthTokenRepository
import com.kappstats.domain.constants.DomainConstants
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.Date

class JwtTokenServiceImpl(
    override val config: TokenConfig,
    private val authTokenRepository: AuthTokenRepository
) : TokenService {

    override fun decode(token: String): Map<String, String>? {
        return try {
            val decodedJWT = verifier().verify(token)
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

    override suspend fun validate(credential: JWTCredential): Any? {
        return if (credential.payload.audience.contains(config.audience)) {
            val principal = JWTPrincipal(credential.payload)
            val authId = principal.getClaim(DomainConstants.AUTH_ID, String::class) ?: return null
            val tokenId = principal.getClaim(DomainConstants.TOKEN_ID, String::class) ?: return null
            val authToken = authTokenRepository.getByAuthId(authId) ?: return null
            if(!authToken.tokens.any { it.id == tokenId && it.isActive }) return null
            principal
        } else null
    }

    override fun verifier(): JWTVerifier =
        JWT.require(Algorithm.HMAC256(config.secret))
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .build()

}
