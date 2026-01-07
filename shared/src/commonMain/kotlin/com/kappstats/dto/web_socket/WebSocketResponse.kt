package com.kappstats.dto.web_socket

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
sealed class WebSocketResponse {

    companion object {
        enum class FailureType {
            Database,
            Exception,
            Network,
            Serialization;
        }
    }

    @Serializable
    data class Failure(val id: String, val action: String, val failureType: FailureType) :
        WebSocketResponse()

    @Serializable
    data class Success(val id: String, val action: String, val data: String) :
        WebSocketResponse()

    val isSuccess: Boolean
        get() = this is Success


}
