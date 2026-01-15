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

    override var webSocketSession: WebSocketSession? = null

    override suspend fun connect(token: String) {
        webSocketSession = remoteDataSource.client.webSocketSession(
            AppEndpoints.WebSocket.Auth.route.replace("http", "ws")
        ) {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
        }
    }

    override suspend fun disconnect() {
        webSocketSession?.close()
        webSocketSession = null
    }

    override suspend fun send(message: String) {
        webSocketSession?.send(message)
    }
}