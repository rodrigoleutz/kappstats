package com.kappstats.domain.web_socket.action

import com.kappstats.di.dataModule
import com.kappstats.domain.model.connection.AuthConnectionInfo
import com.kappstats.domain.model.connection.ConnectionInfo
import com.kappstats.domain.web_socket.contract.WebSocketContract
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse
import kotlinx.serialization.json.Json

class WebSocketActionsImpl : WebSocketActions {

    private val _actions: MutableMap<String, WebSocketContract<Any?, Any?>> = mutableMapOf()
    val actions: Map<String, WebSocketContract<Any?, Any?>> = _actions

    init {
        WsActionsGenerated<Any?, Any?>().list.forEach { action ->
            _actions[action.base.action] = action
        }
    }

    override suspend fun process(
        connectionInfo: ConnectionInfo,
        webSocketRequest: WebSocketRequest
    ): WebSocketResponse? {
        val action = actions[webSocketRequest.action]
            ?: return null
        if (action.base.isAuthAction && connectionInfo !is AuthConnectionInfo) return null
        val data = action.base.inputSerializer?.let { serializer ->
            webSocketRequest.data?.let { data ->
                Json.decodeFromString(serializer, data)
            }
        }
        val process = action.process(connectionInfo, data) ?: return null
        return action.base.outputSerializer?.let {
            WebSocketResponse.Success(
                id = webSocketRequest.id,
                action = webSocketRequest.action,
                data = Json.encodeToString(it, process)
            )
        }
    }
}