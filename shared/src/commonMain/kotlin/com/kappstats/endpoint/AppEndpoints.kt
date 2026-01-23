package com.kappstats.endpoint

import com.kappstats.constants.SERVER_URL

object AppEndpoints : Route(null, SERVER_URL) {

    object Api : Route(AppEndpoints, "/v1") {
        object AppMonitor: Route(Api, "/app_monitor") {
            object Test: Route(AppMonitor, "/test")
        }

        object User : Route(Api, "/user") {
            object Authenticate : Route(User, "/authenticate")
            object SignIn : Route(User, "/sign_in")
            object SignUp : Route(User, "/sign_up")
            object HasUsername : Route(User, "/has_username")
        }
    }

    object WebSocket : Route(AppEndpoints, "/ws") {
        object Auth : Route(WebSocket, "/auth")
        object Dashboard: Route(WebSocket, "/dashboard")
    }
}