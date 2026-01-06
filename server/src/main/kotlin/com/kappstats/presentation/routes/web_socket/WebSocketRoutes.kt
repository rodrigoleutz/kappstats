package com.kappstats.presentation.routes.web_socket

import com.kappstats.domain.web_socket.data.WebSocketData
import com.kappstats.domain.web_socket.model.WebSocketConnection
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.endpoint.AppEndpoints
import com.kappstats.presentation.constants.PresentationConstants
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

fun Route.webSocketRoutes() {
    val webSocketData by inject<WebSocketData>()
    authenticate(PresentationConstants.Auth.JWT) {
        webSocket(AppEndpoints.WebSocket.Auth.fullPath) {

        }
    }
    webSocket(AppEndpoints.WebSocket.path) {
        if(!webSocketData.addConnection(WebSocketConnection.create(this))) {
            close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Add connection fail."))
            return@webSocket
        }
        val job = launch {
            WebSocketEventBus.messages.distinctUntilChanged().collect { message ->
                val json = Json.encodeToString(message)
                send(json)
            }
        }
        runCatching {
            incoming.consumeEach { frame ->
                when (frame) {
                    is Frame.Text -> {
                        try {
                            val json = frame.readText()
                            val jsonDecoded = Json.decodeFromString<WebSocketRequest>(json)
                            WebSocketEventBus.sendMessage(jsonDecoded)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            close(
                                CloseReason(
                                    CloseReason.Codes.NOT_CONSISTENT,
                                    "Disconnect by server."
                                )
                            )
                        }
                    }

                    else -> {
                        println("Unsupported frame: $frame")
                    }
                }
            }
        }.onFailure { e ->
            e.printStackTrace()
        }.also {
            webSocketData.removeConnectionBySession(this)
            job.cancel()
        }
    }
}