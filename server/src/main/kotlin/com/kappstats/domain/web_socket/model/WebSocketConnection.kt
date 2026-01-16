package com.kappstats.domain.web_socket.model

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.domain.model.connection.ConnectionInfo
import com.kappstats.util.IdGenerator
import io.ktor.websocket.DefaultWebSocketSession

data class WebSocketConnection(
    val id: String,
    val session: DefaultWebSocketSession,
    val connectDate: AppDateTime,
    val lastUpdateDate: AppDateTime,
    val connectionInfo: ConnectionInfo
) {
    companion object {
        fun create(
            webSocketId: String,
            connectionInfo: ConnectionInfo,
            defaultWebSocketSession: DefaultWebSocketSession
        ): WebSocketConnection {
            return WebSocketConnection(
                id = webSocketId,
                session = defaultWebSocketSession,
                connectDate = AppDateTime.now,
                lastUpdateDate = AppDateTime.now,
                connectionInfo = connectionInfo
            )
        }
    }
}
