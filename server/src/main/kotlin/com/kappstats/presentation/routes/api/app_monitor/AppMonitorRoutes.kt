package com.kappstats.presentation.routes.api.app_monitor

import com.kappstats.endpoint.AppEndpoints
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.appMonitorRoutes() {
    route(AppEndpoints.Api.AppMonitor.path) {
        get(AppEndpoints.Api.AppMonitor.Test.path) {
            call.respond(HttpStatusCode.OK, "Test AppMonitor route OK")
        }
    }
}