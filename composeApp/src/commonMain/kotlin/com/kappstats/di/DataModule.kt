package com.kappstats.di

import com.kappstats.data.remote.api.RemoteApi
import com.kappstats.data.remote.api.getRemoteApi
import com.kappstats.data.remote.data_source.RemoteDataSource
import com.kappstats.data.remote.data_source.RemoteDataSourceImpl
import com.kappstats.data.service.auth.AuthService
import com.kappstats.data.service.auth.AuthServiceImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single<RemoteApi> { getRemoteApi() }
    singleOf(::RemoteDataSourceImpl) bind RemoteDataSource::class

    factoryOf(::AuthServiceImpl) bind AuthService::class
}