package com.kappstats.domain.web_socket.action

import com.kappstats.domain.model.connection.AuthConnectionInfo
import com.kappstats.domain.model.connection.ConnectionInfo
import com.kappstats.domain.web_socket.contract.WebSocketContract
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse

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
        val process = action.request(connectionInfo, webSocketRequest)
            ?: return null
        return process
    }
}