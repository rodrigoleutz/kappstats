package com.kappstats.presentation.routes.web_socket

import com.kappstats.domain.model.connection.ConnectionInfo
import com.kappstats.domain.web_socket.action.WebSocketActions
import com.kappstats.domain.web_socket.data.WebSocketData
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object WebSocketEventBus : KoinComponent {

    private val wsData by inject<WebSocketData>()
    private val wsActions by inject<WebSocketActions>()

    private val _messages = MutableSharedFlow<WebSocketResponse>(
        replay = 0,
        extraBufferCapacity = 1_000,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val messages = _messages.asSharedFlow()

    private val _authMessages = MutableSharedFlow<WebSocketResponse>(
        replay = 0,
        extraBufferCapacity = 1_000,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val authMessages = _authMessages.asSharedFlow()

    suspend fun sendAuthMessage(
        connectionInfo: ConnectionInfo,
        webSocketRequest: WebSocketRequest
    ) {
        wsActions.process(connectionInfo, webSocketRequest)?.let {
            _authMessages.emit(it)
        }
    }

    suspend fun sendMessage(connectionInfo: ConnectionInfo, webSocketRequest: WebSocketRequest) {
        wsActions.process(connectionInfo, webSocketRequest)?.let {
            _messages.emit(it)
        }
    }
}