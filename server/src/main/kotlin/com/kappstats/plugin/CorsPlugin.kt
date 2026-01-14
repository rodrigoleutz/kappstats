package com.kappstats.plugin

import com.kappstats.constants.config.ProjectConfig
import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

fun Application.configureCors() {
    install(CORS) {
        allowCredentials = true
        anyMethod()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        if(ProjectConfig.GIT_BRANCH != "main") {
            anyHost()
        }
    }
}