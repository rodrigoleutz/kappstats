package com.kappstats.domain.use_case.dashboard

import com.kappstats.data.repository.auth_token.AuthTokenRepository
import com.kappstats.data.service.web_socket.WebSocketService
import com.kappstats.model.dashboard.Dashboard
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class DashboardCollectInfoUseCase(
    private val authTokenRepository: AuthTokenRepository,
    private val webSocketService: WebSocketService
) {
    suspend operator fun invoke(): Flow<Dashboard> {
        val token = authTokenRepository.getToken()
        token?.let {
            webSocketService.dashboardConnect(token)
        }
        return flow {
            webSocketService.dashboardWebSocketSession?.incoming?.consumeAsFlow()
                ?.collect { frame ->
                    val dashboard =
                        if (frame is Frame.Text) Json.decodeFromString<Dashboard>(frame.readText())
                        else return@collect
                    emit(dashboard)
                }
        }
    }
}