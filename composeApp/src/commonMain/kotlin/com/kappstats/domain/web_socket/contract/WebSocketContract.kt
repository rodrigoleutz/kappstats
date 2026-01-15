package com.kappstats.domain.web_socket.contract

import com.kappstats.domain.data_state.DataState
import com.kappstats.domain.web_socket.WebSocketActions
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse
import com.kappstats.dto.web_socket.WsActionBase
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface WebSocketContract<T, R> : KoinComponent {

    val dataState: DataState
        get() = inject<DataState>().value

    val base: WsActionBase<T, R>

    suspend fun receive(webSocketResponse: WebSocketResponse) {
        if (webSocketResponse.isSuccess) {
            base.outputSerializer?.let { serializer ->
                val data = Json.decodeFromString(
                    serializer,
                    (webSocketResponse as WebSocketResponse.Success).data
                )
                process(data)
            }
        }
    }

    suspend fun process(value: R): R?

    suspend fun send(value: T? = null) {
        val webSocketActions by inject<WebSocketActions>()
        val data = base.inputSerializer?.let { serializer ->
            value?.let {
                Json.encodeToString(serializer, it)
            }
        }
        val request = WebSocketRequest(
            action = base.action,
            data = data
        )
        webSocketActions.send(request)
    }
}