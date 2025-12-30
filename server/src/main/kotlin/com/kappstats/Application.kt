package com.kappstats

import com.kappstats.plugin.configureKoin
import com.kappstats.plugin.configureLogger
import com.kappstats.plugin.configureRoutes
import com.kappstats.plugin.configureSecurity
import com.kappstats.plugin.configureSerialization
import com.kappstats.plugin.configureWebSocket
import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(CIO, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureLogger()
    configureSerialization()
    configureKoin()
    configureSecurity()
    configureRoutes()
    configureWebSocket()
}