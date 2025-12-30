package com.kappstats.presentation.util

import com.kappstats.domain.core.resource.Resource
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

suspend fun ApplicationCall.respondFromResource(resource: Resource<*>) {
    this.respond(resource.statusCode, resource.asDataOrNull ?: "")
}