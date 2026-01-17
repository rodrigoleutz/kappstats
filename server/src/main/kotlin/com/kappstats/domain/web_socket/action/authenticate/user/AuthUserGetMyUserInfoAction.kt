package com.kappstats.domain.web_socket.action.authenticate.user

import com.kappstats.data.repository.user.AuthRepository
import com.kappstats.data.repository.user.AuthTokenRepository
import com.kappstats.data.repository.user.ProfileRepository
import com.kappstats.domain.model.connection.AuthConnectionInfo
import com.kappstats.domain.model.connection.ConnectionInfo
import com.kappstats.domain.web_socket.contract.WebSocketContract
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse
import com.kappstats.dto.web_socket.WsAction
import com.kappstats.dto.web_socket.WsActionBase
import com.kappstats.model.user.Auth
import com.kappstats.model.user.AuthToken
import com.kappstats.model.user.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.koin.core.component.inject

@WsAction
object AuthUserGetMyUserInfoAction : WebSocketContract<Any?, Triple<Auth, AuthToken, Profile>> {

    private val authRepository by inject<AuthRepository>()
    private val authTokenRepository by inject<AuthTokenRepository>()
    private val profileRepository by inject<ProfileRepository>()

    val scope = CoroutineScope(Dispatchers.IO)

    override val base: WsActionBase<Any?, Triple<Auth, AuthToken, Profile>> =
        WebSocketEvents.Authenticate.User.GetMyUserInfo

    override suspend fun WebSocketRequest.process(
        connectionInfo: ConnectionInfo,
        data: Any?
    ): WebSocketResponse? {
        if (connectionInfo !is AuthConnectionInfo) return null
        val authJob = scope.async {
            authRepository.generic.getById(connectionInfo.authId)
        }
        val authTokenJob = scope.async {
            authTokenRepository.getByAuthId(connectionInfo.authId)
        }
        val profileJob = scope.async {
            profileRepository.generic.getById(connectionInfo.profileId)
        }
        val auth = authJob.await() ?: return null
        val authToken = authTokenJob.await() ?: return null
        val profile = profileJob.await() ?: return null
        return this.toSuccess(Triple(auth, authToken, profile), profileIdList = listOf(connectionInfo.profileId))
    }
}