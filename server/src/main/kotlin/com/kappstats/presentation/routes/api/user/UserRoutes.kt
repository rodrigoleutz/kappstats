package com.kappstats.presentation.routes.api.user

import com.kappstats.domain.use_case.user.UserUseCases
import com.kappstats.dto.request.user.SignInRequest
import com.kappstats.dto.request.user.SignUpRequest
import com.kappstats.endpoint.AppEndpoints
import com.kappstats.presentation.constants.PresentationConstants
import com.kappstats.presentation.util.respondFromResource
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
    val userUseCases by inject<UserUseCases>()
    route(AppEndpoints.Api.User.path) {
        authenticate(PresentationConstants.Auth.JWT) {
            get(AppEndpoints.Api.User.Authenticate.path) {
                call.respond(HttpStatusCode.OK)
            }
        }
        post<SignInRequest>(AppEndpoints.Api.User.SignIn.path) { request ->
            val resource = userUseCases.signIn.invoke(request)
            call.respondFromResource(resource)
        }
        post<SignUpRequest>(AppEndpoints.Api.User.SignUp.path) { request ->
            val resource = userUseCases.signUp.invoke(request)
            call.respondFromResource(resource)
        }
    }
}