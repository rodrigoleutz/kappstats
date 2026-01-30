package com.kappstats.domain.use_case.auth

import com.kappstats.data.repository.auth_token.AuthTokenRepository
import com.kappstats.data.service.auth.AuthService
import com.kappstats.data.service.web_socket.WebSocketService
import com.kappstats.domain.core.FailureType
import com.kappstats.domain.core.Resource
import com.kappstats.domain.web_socket.WebSocketActions
import com.kappstats.domain.web_socket.actions.apps_monitor.AppsMonitorGetAllAction
import com.kappstats.domain.web_socket.actions.user.AuthUserGetUserInfoAction
import kotlinx.coroutines.delay

class AuthAuthenticateUseCase(
    private val authService: AuthService,
    private val authTokenRepository: AuthTokenRepository,
    private val webSocketActions: WebSocketActions
) {
    suspend operator fun invoke(): Resource<Boolean> {
        return try {
            val token = authTokenRepository.getToken()
                ?: return Resource.Failure(FailureType.LoadData)
            val response = authService.authenticate(token)
            if(!response) return Resource.Failure(FailureType.Unauthorized)
            webSocketActions.connectAndReceiveMessages()
            delay(1_000)
            AuthUserGetUserInfoAction.send()
            AppsMonitorGetAllAction.send()
            Resource.Success(data = true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(FailureType.Network)
        }
    }
}