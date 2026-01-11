package com.kappstats.domain.use_case.auth

data class AuthUseCases(
    val authenticate: AuthAuthenticateUseCase,
    val signIn: AuthSignInUseCase
)
