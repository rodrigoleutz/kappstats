package com.kappstats.presentation.routes.swagget


import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.Route

fun Route.swaggerRoutes() {
    openAPI(path = "openapi") {
    }
    swaggerUI(path = "docs", swaggerFile = "openapi/documentation.yaml")
}