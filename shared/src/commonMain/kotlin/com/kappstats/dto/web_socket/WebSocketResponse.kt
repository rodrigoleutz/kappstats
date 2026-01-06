package com.kappstats.dto.web_socket

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketResponse(
    val id: String,
    val action: WsActionType
)
