package com.kappstats.domain.web_socket.actions.user

import co.touchlab.kermit.Logger
import com.kappstats.domain.web_socket.contract.WebSocketContract
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WsAction
import com.kappstats.dto.web_socket.WsActionBase
import com.kappstats.model.user.Auth
import com.kappstats.model.user.AuthToken
import com.kappstats.model.user.Profile

@WsAction
object AuthUserGetUserInfoAction: WebSocketContract<Any?, Triple<Auth, AuthToken, Profile>>  {

    override val base: WsActionBase<Any?, Triple<Auth, AuthToken, Profile>> =
        WebSocketEvents.Authenticate.User.GetMyUserInfo

    override suspend fun process(value: Triple<Auth, AuthToken, Profile>): Triple<Auth, AuthToken, Profile>? {
        val (auth, token, profile) = value
        dataState.setUserState(
            dataState.user.value.copy(
                myAuth = auth,
                myAuthToken = token,
                myProfile = profile,
                authMap = dataState.user.value.authMap + (auth.id to auth),
                authTokenMap = dataState.user.value.authTokenMap + (token.id to token),
                profileMap = dataState.user.value.profileMap + (profile.id to profile)
            )
        )
        return value
    }
}