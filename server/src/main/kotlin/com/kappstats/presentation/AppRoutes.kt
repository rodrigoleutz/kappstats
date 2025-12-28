package com.kappstats.presentation

import com.kappstats.Greeting
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.appRoutes() {
    get("/") {
        call.respondText("Ktor: ${Greeting().greet()}")
    }
}