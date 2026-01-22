package com.kappstats.plugin

import com.kappstats.di.dataModule
import com.kappstats.di.apiDatabaseModule
import com.kappstats.di.apiEmailModule
import com.kappstats.di.domainModule
import com.kappstats.di.presentationModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(
            apiDatabaseModule,
            apiEmailModule,
            dataModule,
            domainModule,
            presentationModule
        )
    }
}