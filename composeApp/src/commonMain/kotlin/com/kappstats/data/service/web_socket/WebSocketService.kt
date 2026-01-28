package com.kappstats.data.service.web_socket

import io.ktor.websocket.WebSocketSession
import kotlinx.coroutines.isActive

interface WebSocketService {

    var authWebSocketSession: WebSocketSession?
    var dashboardWebSocketSession: WebSocketSession?

    val isConnected: Boolean
        get() = authWebSocketSession != null && authWebSocketSession?.isActive == true
    val dashboardIsConnected: Boolean
        get() = dashboardWebSocketSession != null && dashboardWebSocketSession?.isActive == true
    
    suspend fun connect(token: String)
    suspend fun disconnect()
    suspend fun send(message: String)

    suspend fun dashboardConnect(token: String)
    suspend fun dashboardDisconnect()
    
}