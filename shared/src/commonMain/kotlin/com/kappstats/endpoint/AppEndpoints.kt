package com.kappstats.endpoint

import com.kappstats.constants.SERVER_URL

object AppEndpoints: Route(null, SERVER_URL) {

    object Api: Route(AppEndpoints, "/v1") {
        object User: Route(Api, "/user") {
            object Authenticate: Route(User, "/authenticate")
            object SignIn: Route(User, "/sign_in")
            object SignUp: Route(User, "/sign_up")
        }
    }
}