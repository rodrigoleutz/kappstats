package com.kappstats.di

import com.kappstats.data.data_source.remote.api.email.EmailApi
import com.kappstats.data.data_source.remote.api.email.EmailApiImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val apiEmailModule = module {
    factoryOf(::EmailApiImpl) bind EmailApi::class
}