package com.kappstats.presentation.util

import com.kappstats.custom_object.ip_address.IpAddress
import com.kappstats.domain.constants.DomainConstants
import com.kappstats.domain.core.resource.Resource
import com.kappstats.domain.model.connection.AuthConnectionInfo
import com.kappstats.domain.model.connection.ConnectionInfo
import com.kappstats.domain.model.connection.DefaultConnectionInfo
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.origin
import io.ktor.server.response.respond
import java.net.InetAddress

suspend fun ApplicationCall.respondFromResource(resource: Resource<*>) {
    this.respond(resource.statusCode, resource.asDataOrNull ?: "")
}

fun ApplicationCall.getClientIp(): IpAddress {
    val xRealIp = this.request.headers["X-Real-IP"]?.trim()
    if (!xRealIp.isNullOrBlank()) {
        return IpAddress(xRealIp)
    }
    val cfConnectingIp = this.request.headers["CF-Connecting-IP"]?.trim()
    if (!cfConnectingIp.isNullOrBlank()) {
        return IpAddress(cfConnectingIp)
    }
    val xForwardedFor = this.request.headers["X-Forwarded-For"]
        ?.split(",")?.firstOrNull()?.trim()
    if (!xForwardedFor.isNullOrBlank()) {
        return IpAddress(xForwardedFor)
    }
    return IpAddress(InetAddress.getByName(this.request.origin.remoteHost).hostAddress)
}

fun ApplicationCall.getAuthConnectionInfo(): AuthConnectionInfo? {
    return try {
        val ipAddress = this.getClientIp()
        val jwt = this.principal<JWTPrincipal>()?.payload ?: return null
        val authId = jwt.getClaim(DomainConstants.AUTH_ID)?.asString() ?: return null
        val profileId = jwt.getClaim(DomainConstants.PROFILE_ID)?.asString() ?: return null
        val tokenId = jwt.getClaim(DomainConstants.TOKEN_ID)?.asString() ?: return null
        AuthConnectionInfo(
            ipAddress = ipAddress,
            authId = authId,
            profileId = profileId,
            tokenId = tokenId
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun ApplicationCall.getDefaultConnectionInfo(): DefaultConnectionInfo? {
    return try {
        val ipAddress = this.getClientIp()
        DefaultConnectionInfo(
            ipAddress = ipAddress,
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}