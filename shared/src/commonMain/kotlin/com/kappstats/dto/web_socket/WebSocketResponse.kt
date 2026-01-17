package com.kappstats.dto.web_socket

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonElement

@Serializable
sealed class WebSocketResponse {

    companion object {
        enum class FailureType {
            Conflict,
            Database,
            Exception,
            LoadData,
            Network,
            NoData,
            SaveData,
            Serialization,
            Unauthorized,
            Unknown;
        }
    }

    @Serializable
    data class Failure(
        val id: String,
        val action: String,
        val failureType: FailureType,
        @Transient val webSocketId: String? = null,
        @Transient val profileIdList: List<String> = emptyList()
    ) : WebSocketResponse()

    @Serializable
    data class Success(
        val id: String,
        val action: String,
        val data: String,
        @Transient val webSocketId: String? = null,
        @Transient val profileIdList: List<String> = emptyList()
    ) : WebSocketResponse()

    val isSuccess: Boolean
        get() = this is Success

    val profiles: List<String>
        get() = when(this) {
            is Success -> profileIdList
            is Failure -> profileIdList
        }

    val requestWebSocketId: String
        get() = when(this) {
            is Success -> webSocketId ?: throw IllegalArgumentException("WebSocketId null.")
            is Failure -> webSocketId ?: throw IllegalArgumentException("WebSocketId null.")
        }

}
