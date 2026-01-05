package com.kappstats.domain.web_socket.model

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.util.IdGenerator
import io.ktor.websocket.DefaultWebSocketSession

data class WebSocketConnection(
    val id: String,
    val session: DefaultWebSocketSession,
    val connectDate: AppDateTime,
    val lastUpdateDate: AppDateTime
) {
    companion object {
        fun create(defaultWebSocketSession: DefaultWebSocketSession): WebSocketConnection {
            return WebSocketConnection(
                id = IdGenerator.createUuid,
                session = defaultWebSocketSession,
                connectDate = AppDateTime.now,
                lastUpdateDate = AppDateTime.now
            )
        }
    }
}
