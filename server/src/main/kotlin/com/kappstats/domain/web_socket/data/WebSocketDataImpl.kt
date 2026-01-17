package com.kappstats.domain.web_socket.data

import com.kappstats.domain.model.connection.AuthConnectionInfo
import com.kappstats.domain.web_socket.model.WebSocketConnection
import io.ktor.websocket.DefaultWebSocketSession
import java.util.Collections
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi


class WebSocketDataImpl : WebSocketData {

    private val _authConnections =
        Collections.synchronizedMap<String, WebSocketConnection>(LinkedHashMap())
    override val authConnections: Map<String, WebSocketConnection> = _authConnections
    private val _connections =
        Collections.synchronizedMap<String, WebSocketConnection>(LinkedHashMap())
    override val connections: Map<String, WebSocketConnection> = _connections

    @OptIn(ExperimentalAtomicApi::class)
    private val _messagesSent = AtomicInt(0)

    @OptIn(ExperimentalAtomicApi::class)
    val messagesSent: Int
        get() = _messagesSent.load()

    override fun addConnection(webSocketConnection: WebSocketConnection): Boolean {
        if (webSocketConnection.connectionInfo is AuthConnectionInfo)
            _authConnections[webSocketConnection.id] = webSocketConnection
        else _connections[webSocketConnection.id] = webSocketConnection
        return if (webSocketConnection.connectionInfo is AuthConnectionInfo)
            _authConnections.containsKey(webSocketConnection.id)
        else _connections.containsKey(webSocketConnection.id)
    }

    @OptIn(ExperimentalAtomicApi::class)
    override fun addMessageSent(): Int = _messagesSent.addAndFetch(1)

    override fun removeWebSocketSessionById(id: String): Boolean {
        return _connections.remove(id) != null
    }

    override fun removeConnectionBySession(defaultWebSocketSession: DefaultWebSocketSession): Boolean {
        val remove =
            connections.values.firstOrNull { it.session == defaultWebSocketSession } ?: run {
                val authRemove =
                    authConnections.values.firstOrNull { it.session == defaultWebSocketSession }
                return _authConnections.remove(authRemove?.id) != null
            }
        return _connections.remove(remove.id) != null
    }
}