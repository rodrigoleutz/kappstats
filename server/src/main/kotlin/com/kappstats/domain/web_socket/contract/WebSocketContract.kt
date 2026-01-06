package com.kappstats.domain.web_socket.contract

import com.kappstats.dto.web_socket.WsActionBase
import kotlinx.serialization.json.Json

interface WebSocketContract<T, R> {
    val base: WsActionBase<T, R>
    fun serializeInput(value: T): String? {
        return base.inputSerializer?.let {
            Json.encodeToString(it, value)
        }
    }
}