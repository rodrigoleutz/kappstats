package com.kappstats.di

import com.kappstats.domain.constants.DomainConstants
import com.kappstats.domain.core.security.hashing.HashingService
import com.kappstats.domain.core.security.hashing.SHA256HashingServiceImpl
import com.kappstats.domain.core.security.token.JwtTokenServiceImpl
import com.kappstats.domain.core.security.token.TokenService
import com.kappstats.domain.use_case.user.UserSignInUseCase
import com.kappstats.domain.use_case.user.UserSignUpUseCase
import com.kappstats.domain.use_case.user.UserUseCases
import com.kappstats.domain.web_socket.data.WebSocketData
import com.kappstats.domain.web_socket.data.WebSocketDataImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    singleOf(::SHA256HashingServiceImpl) bind HashingService::class
    single<TokenService> { JwtTokenServiceImpl(DomainConstants.tokenConfig) }

    factoryOf(::UserSignInUseCase)
    factoryOf(::UserSignUpUseCase)
    factoryOf(::UserUseCases)

    singleOf(::WebSocketDataImpl) bind WebSocketData::class
}