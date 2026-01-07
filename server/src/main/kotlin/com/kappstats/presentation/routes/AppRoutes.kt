package com.kappstats.presentation.routes

import com.kappstats.Greeting
import com.kappstats.presentation.routes.api.apiRoutes
import com.kappstats.presentation.routes.web_socket.webSocketRoutes
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.appRoutes() {
    get("/") {
        call.respondText("Ktor: ${Greeting().greet()}")
    }
    apiRoutes()
    webSocketRoutes()
}