package com.kappstats.di

import com.kappstats.data.constants.DataConstants
import com.kappstats.data.data_source.remote.api.database.mongo.MongoApi
import org.koin.dsl.module

val databaseModule = module {
    single {
        MongoApi(
            DataConstants.databaseString,
            DataConstants.databaseName
        )
    }
}