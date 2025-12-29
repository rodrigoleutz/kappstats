package com.kappstats.domain.use_case.user

data class UserUseCases(
    val signIn: UserSignInUseCase,
    val signUp: UserSignUpUseCase
)
