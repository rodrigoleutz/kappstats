package com.kappstats.domain.web_socket.contract

import com.kappstats.domain.model.connection.AuthConnectionInfo
import com.kappstats.domain.model.connection.ConnectionInfo
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse
import com.kappstats.dto.web_socket.WsActionBase
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent

interface WebSocketContract<T, R> : KoinComponent {
    val base: WsActionBase<T, R>
    suspend fun WebSocketRequest.process(
        connectionInfo: ConnectionInfo,
        data: T?
    ): WebSocketResponse?

    suspend fun request(
        connectionInfo: ConnectionInfo,
        webSocketRequest: WebSocketRequest
    ): WebSocketResponse? {
        if (base.isAuthAction && connectionInfo !is AuthConnectionInfo) return null
        val data = base.inputSerializer?.let { serializer ->
            webSocketRequest.data?.let { data ->
                Json.decodeFromString(serializer, data)
            }
        }
        val process = webSocketRequest.process(connectionInfo, data)
        return process
    }

    fun WebSocketRequest.toSuccess(
        data: R?,
        profileIdList: List<String> = emptyList(),
    ): WebSocketResponse.Success {
        val json = base.outputSerializer?.let { serializer ->
            data?.let { Json.encodeToString(serializer, it) }
        } ?: ""
        return WebSocketResponse.Success(
            id = this.id,
            webSocketId = this.webSocketId,
            action = this.action,
            data = json,
            profileIdList = profileIdList
        )
    }

    fun WebSocketRequest.toFailure(
        failure: WebSocketResponse.Companion.FailureType,
        profileIdList: List<String> = emptyList(),
    ): WebSocketResponse.Failure = WebSocketResponse.Failure(
        id = this.id,
        webSocketId = this.webSocketId,
        action = this.action,
        profileIdList = profileIdList,
        failureType = failure
    )
}