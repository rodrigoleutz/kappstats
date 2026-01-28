package com.kappstats.presentation.routes.web_socket

import com.kappstats.domain.model.connection.DashboardConnectionInfo
import com.kappstats.domain.web_socket.data.WebSocketData
import com.kappstats.domain.web_socket.model.WebSocketConnection
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.endpoint.AppEndpoints
import com.kappstats.presentation.constants.PresentationConstants
import com.kappstats.presentation.util.getAuthConnectionInfo
import com.kappstats.presentation.util.getDefaultConnectionInfo
import com.kappstats.util.IdGenerator
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import jdk.jfr.internal.OldObjectSample.emit
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

fun Route.webSocketRoutes() {
    val webSocketData by inject<WebSocketData>()
    authenticate(PresentationConstants.Auth.JWT) {
        webSocket(AppEndpoints.WebSocket.Dashboard.fullPath) {
            val webSocketId = IdGenerator.createUuid
            //TODO: Verify dashboard permission for user
            val connectionInfo = DashboardConnectionInfo.fromAuthConnectionInfo(
                call.getAuthConnectionInfo(webSocketId)
            ) ?: run {
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Add connection info fail."))
                return@webSocket
            }
            val webSocketConnection = WebSocketConnection.create(
                webSocketId,
                connectionInfo,
                this
            )
            if (!webSocketData.addConnection(webSocketConnection)) {
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Add connection fail."))
                return@webSocket
            }
            val job = launch {
                WebSocketEventBus.dashboardMessages.distinctUntilChanged().collect { message ->
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
                                WebSocketEventBus.sendAuthMessage(
                                    connectionInfo, jsonDecoded.copy(
                                        webSocketId = webSocketId,
                                        profileId = connectionInfo.profileId
                                    )
                                )
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
        webSocket(AppEndpoints.WebSocket.Auth.fullPath) {
            val webSocketId = IdGenerator.createUuid
            val connectionInfo = call.getAuthConnectionInfo(webSocketId) ?: run {
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Add connection info fail."))
                return@webSocket
            }
            if (!webSocketData.addConnection(
                    WebSocketConnection.create(
                        webSocketId,
                        connectionInfo,
                        this
                    )
                )
            ) {
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Add connection fail."))
                return@webSocket
            }
            val job = launch {
                WebSocketEventBus.authMessages.distinctUntilChanged().collect { message ->
                    when {
                        connectionInfo.profileId in message.profiles -> {
                            val json = Json.encodeToString(message)
                            send(json)
                        }

                        message.profiles.isEmpty() && message.requestWebSocketId == connectionInfo.webSocketId -> {
                            val json = Json.encodeToString(message)
                            send(json)
                        }
                    }
                }
            }
            runCatching {
                incoming.consumeEach { frame ->
                    when (frame) {
                        is Frame.Text -> {
                            try {
                                val json = frame.readText()
                                val jsonDecoded = Json.decodeFromString<WebSocketRequest>(json)
                                WebSocketEventBus.sendAuthMessage(
                                    connectionInfo, jsonDecoded.copy(
                                        webSocketId = webSocketId,
                                        profileId = connectionInfo.profileId
                                    )
                                )
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
    webSocket(AppEndpoints.WebSocket.path) {
        val webSocketId = IdGenerator.createUuid
        val connectionInfo = call.getDefaultConnectionInfo(webSocketId) ?: run {
            close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Add connection info fail."))
            return@webSocket
        }
        if (!webSocketData.addConnection(
                WebSocketConnection.create(
                    webSocketId,
                    connectionInfo,
                    this
                )
            )
        ) {
            close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Add connection fail."))
            return@webSocket
        }
        val job = launch {
            WebSocketEventBus.messages.distinctUntilChanged().collect { message ->
                if (webSocketId == message.requestWebSocketId) {
                    val json = Json.encodeToString(message)
                    send(json)
                }
            }
        }
        runCatching {
            incoming.consumeEach { frame ->
                when (frame) {
                    is Frame.Text -> {
                        try {
                            val json = frame.readText()
                            val jsonDecoded = Json.decodeFromString<WebSocketRequest>(json)
                            WebSocketEventBus.sendMessage(
                                connectionInfo,
                                jsonDecoded.copy(webSocketId = webSocketId)
                            )
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