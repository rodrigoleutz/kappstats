package com.kappstats.presentation.routes.web_socket

import com.kappstats.dto.web_socket.WebSocketResponse
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object WebSocketEventBus {

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

}