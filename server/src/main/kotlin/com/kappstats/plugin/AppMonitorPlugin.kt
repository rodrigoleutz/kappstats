package com.kappstats.plugin

import com.kappstats.plugin.custom.AppMonitor
import io.ktor.server.application.Application
import io.ktor.server.application.install

fun Application.configureAppMonitor() {
    install(AppMonitor) {
        onReceived = { text ->

        }
    }
}