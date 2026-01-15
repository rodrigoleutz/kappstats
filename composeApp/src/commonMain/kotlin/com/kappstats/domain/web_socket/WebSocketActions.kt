package com.kappstats.domain.web_socket

import com.kappstats.dto.web_socket.WebSocketRequest

interface WebSocketActions {

    fun connectAndReceiveMessages()

    suspend fun send(webSocketRequest: WebSocketRequest)
}