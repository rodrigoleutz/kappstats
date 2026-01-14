package com.kappstats.di

import com.kappstats.data.local.data_source.LocalDataSource
import com.kappstats.data.local.data_source.LocalDataSourceImpl
import com.kappstats.data.local.security.CryptManager
import com.kappstats.data.local.security.getCryptManager
import com.kappstats.data.remote.api.RemoteApi
import com.kappstats.data.remote.api.getRemoteApi
import com.kappstats.data.remote.data_source.RemoteDataSource
import com.kappstats.data.remote.data_source.RemoteDataSourceImpl
import com.kappstats.data.repository.auth_token.AuthTokenRepository
import com.kappstats.data.repository.auth_token.AuthTokenRepositoryImpl
import com.kappstats.data.service.auth.AuthService
import com.kappstats.data.service.auth.AuthServiceImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single<CryptManager> { getCryptManager() }
    singleOf(::LocalDataSourceImpl) bind LocalDataSource::class
    single<RemoteApi> { getRemoteApi() }
    singleOf(::RemoteDataSourceImpl) bind RemoteDataSource::class

    singleOf(::AuthTokenRepositoryImpl) bind AuthTokenRepository::class

    factoryOf(::AuthServiceImpl) bind AuthService::class
}