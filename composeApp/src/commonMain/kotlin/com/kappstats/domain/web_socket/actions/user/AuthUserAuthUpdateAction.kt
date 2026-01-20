package com.kappstats.domain.web_socket.actions.user

import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.domain.web_socket.contract.WebSocketContract
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WsAction
import com.kappstats.dto.web_socket.WsActionBase
import com.kappstats.model.user.Auth

@WsAction
object AuthUserAuthUpdateAction : WebSocketContract<Pair<Pair<Email, Password?>, Password>, Auth?> {

    override val base: WsActionBase<Pair<Pair<Email, Password?>, Password>, Auth?> =
        WebSocketEvents.Authenticate.User.AuthUpdate

    override suspend fun process(value: Auth?): Auth? {
        value?.let { auth ->
            dataState.setUserState(
                dataState.user.value.copy(
                    myAuth = auth,
                    authMap = dataState.user.value.authMap + (auth.id to auth)
                )
            )
        }
        return value
    }
}