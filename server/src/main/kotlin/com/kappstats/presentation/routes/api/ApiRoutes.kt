package com.kappstats.presentation.routes.api

import com.kappstats.endpoint.AppEndpoints
import com.kappstats.presentation.routes.api.app_monitor.appMonitorRoutes
import com.kappstats.presentation.routes.api.user.userRoutes
import io.ktor.server.routing.Route
import io.ktor.server.routing.route

fun Route.apiRoutes() {
    route(AppEndpoints.Api.path) {
        appMonitorRoutes()
        userRoutes()
    }
}