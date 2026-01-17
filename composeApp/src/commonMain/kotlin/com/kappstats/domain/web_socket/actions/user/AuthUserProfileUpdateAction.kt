package com.kappstats.domain.web_socket.actions.user

import com.kappstats.domain.web_socket.contract.WebSocketContract
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WsAction
import com.kappstats.dto.web_socket.WsActionBase
import com.kappstats.model.user.Profile

@WsAction
object AuthUserProfileUpdateAction: WebSocketContract<Profile, Profile?> {

    override val base: WsActionBase<Profile, Profile?> =
        WebSocketEvents.Authenticate.User.ProfileUpdate

    override suspend fun process(value: Profile?): Profile? {
        value?.let { profile ->
            val profileMap = dataState.user.value.profileMap.toMutableMap()
            profileMap[profile.id] = profile
            dataState.setUserState(
                dataState.user.value.copy(
                    myProfile = profile,
                    profileMap = profileMap
                )
            )
        }
        return value
    }
}