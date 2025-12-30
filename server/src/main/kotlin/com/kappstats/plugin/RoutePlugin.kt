package com.kappstats.plugin

import com.kappstats.presentation.routes.appRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRoutes() {
    routing {
        appRoutes()
    }
}