package com.kappstats.plugin

import com.kappstats.constants.config.ProjectConfig
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.origin
import org.slf4j.event.Level

fun Application.configureLogger() {
    install(CallLogging) {
        level = if(ProjectConfig.GIT_BRANCH == "main") Level.INFO else Level.DEBUG
        mdc("ip") { call -> call.request.origin.remoteHost }
        mdc("userAgent") { call -> call.request.headers["User-Agent"] }
    }
}