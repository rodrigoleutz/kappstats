package com.kappstats.domain.web_socket.action

import com.kappstats.domain.model.connection.ConnectionInfo
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse

interface WebSocketActions {

    suspend fun process(
        connectionInfo: ConnectionInfo,
        webSocketRequest: WebSocketRequest
    ): WebSocketResponse?
}