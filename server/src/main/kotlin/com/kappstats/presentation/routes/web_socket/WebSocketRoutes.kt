package com.kappstats.presentation.routes.web_socket

import com.kappstats.domain.web_socket.data.WebSocketData
import com.kappstats.domain.web_socket.model.WebSocketConnection
import com.kappstats.endpoint.AppEndpoints
import com.kappstats.presentation.constants.PresentationConstants
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.websocket.webSocket
import org.koin.ktor.ext.inject

fun Route.webSocketRoutes() {
    val webSocketData by inject<WebSocketData>()
    authenticate(PresentationConstants.Auth.JWT) {
        webSocket(AppEndpoints.WebSocket.Auth.fullPath) {

        }
    }
    webSocket(AppEndpoints.WebSocket.path) {
        if(!webSocketData.addConnection(WebSocketConnection.create(this))) {
            call.respond(HttpStatusCode.BadRequest)
            return@webSocket
        }
        runCatching {

        }.onFailure {

        }.also {

        }
    }
}