package com.kappstats.di

import com.kappstats.data.data_source.local.host_system_monitor.HostSystemMonitor
import com.kappstats.data.data_source.local.host_system_monitor.HostSystemMonitorImpl
import com.kappstats.data.data_source.remote.api.email.EmailApi
import com.kappstats.data.data_source.remote.api.email.EmailApiImpl
import com.kappstats.data.repository.user.AuthRepository
import com.kappstats.data.repository.user.AuthRepositoryImpl
import com.kappstats.data.repository.user.AuthTokenRepository
import com.kappstats.data.repository.user.AuthTokenRepositoryImpl
import com.kappstats.data.repository.user.ProfileRepository
import com.kappstats.data.repository.user.ProfileRepositoryImpl
import com.kappstats.data.service.host_system_monitor.HostSystemMonitorService
import com.kappstats.data.service.host_system_monitor.HostSystemMonitorServiceImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::AuthTokenRepositoryImpl) bind AuthTokenRepository::class
    singleOf(::ProfileRepositoryImpl) bind ProfileRepository::class

    single<HostSystemMonitor> {
        HostSystemMonitorImpl(System.getenv("PROC_DIR"))
    }
    single<HostSystemMonitorService> {
        HostSystemMonitorServiceImpl(get())
    }
}