package com.kappstats.domain.web_socket.action

import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse

interface WebSocketActions {

    fun process(webSocketRequest: WebSocketRequest): WebSocketResponse
}