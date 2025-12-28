package com.kappstats.di

import com.kappstats.data.constants.DataConstants
import com.kappstats.data.remote.api.database.mongo.MongoApi
import org.koin.dsl.module

val dataModule = module {
    single {
        MongoApi(
            DataConstants.databaseString,
            DataConstants.databaseName
        )
    }
}