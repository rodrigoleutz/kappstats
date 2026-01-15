package com.kappstats.domain.web_socket

import co.touchlab.kermit.Logger
import com.kappstats.data.repository.auth_token.AuthTokenRepository
import com.kappstats.data.service.web_socket.WebSocketService
import com.kappstats.domain.web_socket.contract.WebSocketContract
import com.kappstats.domain.web_socket.generated.WsActionsGenerated
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class WebSocketActionsImpl(
    private val authTokenRepository: AuthTokenRepository,
    private val webSocketService: WebSocketService
) : WebSocketActions {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val actionsMap: MutableMap<String, WebSocketContract<Any?, Any?>> = mutableMapOf()

    init {
        WsActionsGenerated<Any?, Any?>().list.forEach { actionsMap[it.base.action] = it }
    }

    override fun connectAndReceiveMessages() {
        scope.launch {
            val token = authTokenRepository.getToken() ?: return@launch
            webSocketService.connect(token)
            webSocketService.webSocketSession?.incoming
                ?.receiveAsFlow()
                ?.cancellable()
                ?.filterIsInstance<Frame.Text>()
                ?.map { frameText ->
                    Json.decodeFromString<WebSocketResponse>(frameText.readText())
                }?.collect { webSocketResponse ->
                    if (webSocketResponse.isSuccess) {
                        val action =
                            actionsMap[(webSocketResponse as WebSocketResponse.Success).action]
                        action?.receive(webSocketResponse)
                    }
                }
        }
    }

    override suspend fun send(webSocketRequest: WebSocketRequest) {
        val json = Json.encodeToString(webSocketRequest)
        webSocketService.send(json)
    }
}