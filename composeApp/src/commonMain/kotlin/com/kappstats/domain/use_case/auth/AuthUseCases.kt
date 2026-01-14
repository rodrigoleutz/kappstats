package com.kappstats.domain.use_case.auth

data class AuthUseCases(
    val authenticate: AuthAuthenticateUseCase,
    val hasUsername: AuthHasUsernameUseCase,
    val logout: AuthLogoutUseCase,
    val signIn: AuthSignInUseCase,
    val signUp: AuthSignUpUseCase
)
