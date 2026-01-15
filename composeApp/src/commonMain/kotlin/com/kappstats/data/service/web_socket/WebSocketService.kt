package com.kappstats.data.service.web_socket

import io.ktor.websocket.WebSocketSession
import kotlinx.coroutines.isActive

interface WebSocketService {

    var webSocketSession: WebSocketSession?

    val isConnected: Boolean
        get() = webSocketSession != null && webSocketSession?.isActive == true
    
    suspend fun connect(token: String)
    suspend fun disconnect()
    
    suspend fun send(message: String)
    
}