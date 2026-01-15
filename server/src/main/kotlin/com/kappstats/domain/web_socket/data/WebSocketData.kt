package com.kappstats.domain.web_socket.data

import com.kappstats.domain.web_socket.model.WebSocketConnection
import io.ktor.websocket.DefaultWebSocketSession

interface WebSocketData {
    val connections: Map<String, WebSocketConnection>
    val authConnections: Map<String, WebSocketConnection>

    fun addConnection(webSocketConnection: WebSocketConnection): Boolean
    fun addMessageSent(): Int
    fun removeWebSocketSessionById(id: String): Boolean
    fun removeConnectionBySession(defaultWebSocketSession: DefaultWebSocketSession): Boolean
}