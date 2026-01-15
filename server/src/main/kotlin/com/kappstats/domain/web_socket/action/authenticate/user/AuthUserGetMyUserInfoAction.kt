package com.kappstats.domain.web_socket.action.authenticate.user

import com.kappstats.data.repository.user.AuthRepository
import com.kappstats.data.repository.user.AuthTokenRepository
import com.kappstats.data.repository.user.ProfileRepository
import com.kappstats.domain.model.connection.AuthConnectionInfo
import com.kappstats.domain.model.connection.ConnectionInfo
import com.kappstats.domain.web_socket.contract.WebSocketContract
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WsAction
import com.kappstats.dto.web_socket.WsActionBase
import com.kappstats.model.user.Auth
import com.kappstats.model.user.AuthToken
import com.kappstats.model.user.Profile
import org.koin.core.component.inject

@WsAction
object AuthUserGetMyUserInfoAction : WebSocketContract<Any?, Triple<Auth, AuthToken, Profile>> {

    private val authRepository by inject<AuthRepository>()
    private val authTokenRepository by inject<AuthTokenRepository>()
    private val profileRepository by inject<ProfileRepository>()

    override val base: WsActionBase<Any?, Triple<Auth, AuthToken, Profile>> =
        WebSocketEvents.Authenticate.User.GetMyUserInfo

    override suspend fun process(
        connectionInfo: ConnectionInfo,
        data: Any?
    ): Triple<Auth, AuthToken, Profile>? {
        if (connectionInfo !is AuthConnectionInfo) return null
        val auth = authRepository.generic.getById(connectionInfo.authId) ?: return null
        val authToken = authTokenRepository.getByAuthId(connectionInfo.authId) ?: return null
        val profile = profileRepository.generic.getById(connectionInfo.profileId) ?: return null
        return Triple(auth, authToken, profile)
    }
}