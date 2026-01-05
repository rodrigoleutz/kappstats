package com.kappstats.domain.web_socket.data

import com.kappstats.domain.web_socket.model.WebSocketConnection
import io.ktor.websocket.DefaultWebSocketSession
import java.util.Collections


class WebSocketDataImpl : WebSocketData {
    private val _connections =
        Collections.synchronizedMap<String, WebSocketConnection>(LinkedHashMap())
    override val connections: Map<String, WebSocketConnection> = _connections

    override fun addConnection(webSocketConnection: WebSocketConnection): Boolean {
        _connections[webSocketConnection.id] = webSocketConnection
        return connections.containsKey(webSocketConnection.id)
    }

    override fun removeWebSocketSessionById(id: String): Boolean {
        return _connections.remove(id) != null
    }

    override fun removeConnectionBySession(defaultWebSocketSession: DefaultWebSocketSession): Boolean {
        val remove = connections.values.first { it.session == defaultWebSocketSession }
        return _connections.remove(remove.id) != null
    }
}