package com.kappstats.domain.constants

import com.kappstats.domain.core.security.token.TokenConfig

object DomainConstants {

    val jwtIssuer = System.getenv()["JWT_ISSUER"] ?: "issuer"
    val jwtAudience = System.getenv()["JWT_AUDIENCE"] ?: "audience"
    val jwtRealm = System.getenv()["JWT_REALM"] ?: "realm"
    val jwtSecret = System.getenv()["JWT_SECRET"] ?: "secret"

    val tokenConfig = TokenConfig(
        issuer = jwtIssuer,
        audience = jwtAudience,
        realm = jwtRealm,
        secret = jwtSecret
    )
}