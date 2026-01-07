package com.kappstats.domain.web_socket.action

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

    override fun process(webSocketRequest: WebSocketRequest): WebSocketResponse? {
        val action = actions[webSocketRequest.action]
            ?: return null
        val data = action.base.inputSerializer?.let {
            Json.decodeFromString(it, webSocketRequest.data)
        } ?: return null
        val process = action.process(data) ?: return null
        return action.base.outputSerializer?.let {
            WebSocketResponse.Success(
                id = webSocketRequest.id,
                action = webSocketRequest.action,
                data = Json.encodeToString(it, process)
            )
        }
    }
}