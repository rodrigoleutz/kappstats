package com.kappstats.domain.web_socket

import com.kappstats.dto.web_socket.WebSocketRequest
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.sync.Mutex

interface WebSocketActions {

    val mutex: Mutex
    val pendingRequest: MutableMap<String, CompletableDeferred<Any?>>

    fun connectAndReceiveMessages()

    suspend fun send(webSocketRequest: WebSocketRequest)
}