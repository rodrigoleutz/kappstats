package com.kappstats.data.service.web_socket

import com.kappstats.data.remote.data_source.RemoteDataSource
import com.kappstats.endpoint.AppEndpoints
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.bearerAuth
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.send

class WebSocketServiceImpl(
    private val remoteDataSource: RemoteDataSource
) : WebSocketService {

    override var authWebSocketSession: WebSocketSession? = null
    override var dashboardWebSocketSession: WebSocketSession? = null

    override suspend fun connect(token: String) {
        authWebSocketSession = remoteDataSource.client.webSocketSession(
            AppEndpoints.WebSocket.Auth.route.replaceFirst("http", "ws")
        ) {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
        }
    }

    override suspend fun disconnect() {
        authWebSocketSession?.close()
        authWebSocketSession = null
    }

    override suspend fun send(message: String) {
        authWebSocketSession?.send(message)
    }

    override suspend fun dashboardConnect(token: String) {
        dashboardWebSocketSession = remoteDataSource.client.webSocketSession(
            AppEndpoints.WebSocket.Dashboard.route.replaceFirst("http", "ws")
        ) {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
        }
    }

    override suspend fun dashboardDisconnect() {
        dashboardWebSocketSession?.close()
        dashboardWebSocketSession = null
    }
}