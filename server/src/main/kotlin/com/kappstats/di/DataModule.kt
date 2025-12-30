package com.kappstats.di

import com.kappstats.data.repository.user.AuthRepository
import com.kappstats.data.repository.user.AuthRepositoryImpl
import com.kappstats.data.repository.user.ProfileRepository
import com.kappstats.data.repository.user.ProfileRepositoryImpl
import com.kappstats.data.service.email.EmailService
import com.kappstats.data.service.email.EmailServiceImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::ProfileRepositoryImpl) bind ProfileRepository::class
    factoryOf(::EmailServiceImpl) bind EmailService::class
}