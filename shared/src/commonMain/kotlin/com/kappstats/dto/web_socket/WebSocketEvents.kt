package com.kappstats.dto.web_socket

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.model.user.Auth
import com.kappstats.model.user.AuthToken
import com.kappstats.model.user.Profile
import kotlinx.serialization.builtins.TripleSerializer
import kotlinx.serialization.builtins.nullable

object WebSocketEvents : WsActionBase<Any?, Any?>(null, "/web_socket", isAuth = false) {

    /**
     * Authenticated Events
     *
     * Use object Authenticate
     */
    object Authenticate : WsActionBase<Any?, Any?>(WebSocketEvents, "/auth") {

        object User : WsActionBase<Any?, Any?>(Authenticate, "/user") {
            object GetMyUserInfo :
                WsActionBase<Any?, Triple<Auth, AuthToken, Profile>>(
                    parent= User,
                    command = "/get_my_user_info",
                    inputSerializer = null,
                    outputSerializer = TripleSerializer(
                        Auth.serializer(),
                        AuthToken.serializer(),
                        Profile.serializer()
                    ),
                    isAuth = true
                )

            object ProfileUpdate: WsActionBase<Profile, Profile?>(
                parent = User,
                command = "/profile_update",
                inputSerializer = Profile.serializer(),
                outputSerializer = Profile.serializer().nullable,
                isAuth = true
            )
        }
    }

    /**
     * Unauthenticated Events
     */
    object Status : WsActionBase<Any?, Any?>(WebSocketEvents, "/status", isAuth = false) {

        object Ping : WsActionBase<AppDateTime, AppDateTime>(
            parent = Status,
            command = "/ping",
            inputSerializer = AppDateTime.serializer(),
            outputSerializer = AppDateTime.serializer(),
            isAuth = false
        )
    }
}