package com.kappstats.di

import com.kappstats.data.constants.DataConstants
import com.kappstats.data.remote.api.database.mongo.MongoApi
import com.kappstats.data.repository.user.AuthRepository
import com.kappstats.data.repository.user.AuthRepositoryImpl
import com.kappstats.data.repository.user.ProfileRepository
import com.kappstats.data.repository.user.ProfileRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single {
        MongoApi(
            DataConstants.databaseString,
            DataConstants.databaseName
        )
    }
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::ProfileRepositoryImpl) bind ProfileRepository::class
}