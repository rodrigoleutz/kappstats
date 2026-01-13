package com.kappstats.di

import com.kappstats.domain.use_case.auth.AuthAuthenticateUseCase
import com.kappstats.domain.use_case.auth.AuthHasUsernameUseCase
import com.kappstats.domain.use_case.auth.AuthSignInUseCase
import com.kappstats.domain.use_case.auth.AuthSignUpUseCase
import com.kappstats.domain.use_case.auth.AuthUseCases
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::AuthAuthenticateUseCase)
    factoryOf(::AuthHasUsernameUseCase)
    factoryOf(::AuthSignInUseCase)
    factoryOf(::AuthSignUpUseCase)
    factoryOf(::AuthUseCases)
}