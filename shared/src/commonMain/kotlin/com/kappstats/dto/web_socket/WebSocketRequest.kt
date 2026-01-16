package com.kappstats.dto.web_socket

import com.kappstats.util.IdGenerator
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
data class WebSocketRequest(
    val id: String = IdGenerator.createUuid,
    val action: String,
    val data: String? = null,
    @Transient val webSocketId: String? = null,
    @Transient val profileId: String? = null
)
