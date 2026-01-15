package com.kappstats.dto.web_socket

import com.kappstats.util.IdGenerator
import kotlinx.serialization.Serializable


@Serializable
data class WebSocketRequest(
    val id: String = IdGenerator.createUuid,
    val action: String,
    val data: String? = null
)
