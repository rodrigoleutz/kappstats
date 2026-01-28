package com.kappstats.di

import com.kappstats.domain.data_state.DataState
import com.kappstats.domain.data_state.DataStateImpl
import com.kappstats.domain.use_case.auth.AuthAuthenticateUseCase
import com.kappstats.domain.use_case.auth.AuthHasUsernameUseCase
import com.kappstats.domain.use_case.auth.AuthLogoutUseCase
import com.kappstats.domain.use_case.auth.AuthSignInUseCase
import com.kappstats.domain.use_case.auth.AuthSignUpUseCase
import com.kappstats.domain.use_case.auth.AuthUseCases
import com.kappstats.domain.use_case.dashboard.DashboardCollectInfoUseCase
import com.kappstats.domain.use_case.dashboard.DashboardUseCases
import com.kappstats.domain.web_socket.WebSocketActions
import com.kappstats.domain.web_socket.WebSocketActionsImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    single<DataState> { DataStateImpl() }

    factoryOf(::AuthAuthenticateUseCase)
    factoryOf(::AuthHasUsernameUseCase)
    factoryOf(::AuthLogoutUseCase)
    factoryOf(::AuthSignInUseCase)
    factoryOf(::AuthSignUpUseCase)
    factoryOf(::AuthUseCases)

    factoryOf(::DashboardCollectInfoUseCase)
    factoryOf(::DashboardUseCases)

    singleOf(::WebSocketActionsImpl) bind WebSocketActions::class
}