package com.kappstats.dto.web_socket

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.model.app.AppMonitor
import com.kappstats.model.user.Auth
import com.kappstats.model.user.AuthToken
import com.kappstats.model.user.Profile
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.builtins.TripleSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer

object WebSocketEvents : WsActionBase<Any?, Any?>(null, "/web_socket", isAuth = false) {

    /**
     * Authenticated Events
     *
     * Use object Authenticate
     */
    object Authenticate : WsActionBase<Any?, Any?>(WebSocketEvents, "/auth") {

        object AppsMonitor : WsActionBase<Any?, Any?>(Authenticate, "/apps_monitor") {

            object Add : WsActionBase<AppMonitor, AppMonitor?>(
                parent = AppsMonitor,
                command = "/add",
                inputSerializer = AppMonitor.serializer(),
                outputSerializer = AppMonitor.serializer().nullable,
                isAuth = true
            )

            object Delete : WsActionBase<String, String?>(
                parent = AppsMonitor,
                command = "/delete",
                inputSerializer = String.serializer(),
                outputSerializer = String.serializer().nullable,
                isAuth = true
            )

            object GetAll : WsActionBase<Any?, List<AppMonitor>>(
                parent = AppsMonitor,
                command = "/get_all",
                inputSerializer = null,
                outputSerializer = ListSerializer(AppMonitor.serializer()),
                isAuth = true
            )

            object Update : WsActionBase<AppMonitor, AppMonitor?>(
                parent = AppsMonitor,
                command = "/update",
                inputSerializer = AppMonitor.serializer(),
                outputSerializer = AppMonitor.serializer().nullable,
                isAuth = true
            )

        }

        object User : WsActionBase<Any?, Any?>(Authenticate, "/user") {

            object AuthUpdate : WsActionBase<Pair<Pair<Email, Password?>, Password>, Auth?>(
                parent = User,
                command = "/auth_update",
                inputSerializer = PairSerializer(
                    PairSerializer(
                        Email.serializer(),
                        Password.serializer().nullable
                    ), Password.serializer()
                ),
                outputSerializer = Auth.serializer().nullable,
                isAuth = true
            )

            object GetMyUserInfo :
                WsActionBase<Any?, Triple<Auth, AuthToken, Profile>>(
                    parent = User,
                    command = "/get_my_user_info",
                    inputSerializer = null,
                    outputSerializer = TripleSerializer(
                        Auth.serializer(),
                        AuthToken.serializer(),
                        Profile.serializer()
                    ),
                    isAuth = true
                )

            object ProfileUpdate : WsActionBase<Profile, Profile?>(
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