package com.kappstats.domain.web_socket.contract

import com.kappstats.domain.data_state.DataState
import com.kappstats.domain.web_socket.WebSocketActions
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse
import com.kappstats.dto.web_socket.WsActionBase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface WebSocketContract<T, R> : KoinComponent {

    val dataState: DataState
        get() = inject<DataState>().value

    val base: WsActionBase<T, R>

    suspend fun process(value: R): R?

    suspend fun receive(webSocketResponse: WebSocketResponse) {
        try {
            val webSocketActions by inject<WebSocketActions>()
            val deferred = webSocketActions.mutex.withLock {
                webSocketActions.pendingRequest.remove(webSocketResponse.requestId)
            }
            if (webSocketResponse is WebSocketResponse.Success) {
                base.outputSerializer?.let { serializer ->
                    val data = Json.decodeFromString(
                        serializer,
                        webSocketResponse.data
                    )
                    process(data)
                    deferred?.complete(data)
                }
            } else {
                val failure = (webSocketResponse as WebSocketResponse.Failure).failureType
                deferred?.complete(failure)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun send(value: T? = null): R? {
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
        val deferred = CompletableDeferred<Any?>()
        webSocketActions.mutex.withLock { webSocketActions.pendingRequest[request.id] = deferred }
        return try {
            webSocketActions.send(request)
            withTimeoutOrNull(10_000) {
                deferred.await() as? R
            }
        } catch (e: Exception) {
            //TODO:DataState error UI
            e.printStackTrace()
            null
        } finally {
            webSocketActions.mutex.withLock { webSocketActions.pendingRequest.remove(request.id) }
        }
    }
}