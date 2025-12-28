package com.kappstats.plugin

import com.kappstats.constants.config.ProjectConfig
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import org.slf4j.event.Level

fun Application.configureLogger() {
    install(CallLogging) {
        level = if(ProjectConfig.GIT_BRANCH != "main") Level.DEBUG else Level.TRACE
    }
}