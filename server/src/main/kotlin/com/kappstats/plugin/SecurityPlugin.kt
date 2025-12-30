package com.kappstats.plugin

import com.kappstats.domain.core.security.token.TokenService
import com.kappstats.presentation.constants.PresentationConstants
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val tokenService by inject<TokenService>()
    install(Authentication) {
        jwt(PresentationConstants.Auth.JWT) {
            realm = tokenService.config.realm
            verifier(tokenService.verifier())
            validate { credential ->
                tokenService.validate(credential)
            }
        }
    }
}