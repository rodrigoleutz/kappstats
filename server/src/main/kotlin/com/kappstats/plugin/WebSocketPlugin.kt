package com.kappstats.plugin

import com.kappstats.constants.config.ProjectConfig
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.websocket.WebSocketDeflateExtension
import kotlin.time.Duration.Companion.seconds

fun Application.configureWebSocket() {
    install(WebSockets) {
        pingPeriod = 15.seconds
        timeout = 30.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
        extensions {
            install(WebSocketDeflateExtension) {
                compressionLevel = 6
                clientNoContextTakeOver = ProjectConfig.GIT_BRANCH != "main"
                serverNoContextTakeOver = ProjectConfig.GIT_BRANCH != "main"
            }
        }
    }
}