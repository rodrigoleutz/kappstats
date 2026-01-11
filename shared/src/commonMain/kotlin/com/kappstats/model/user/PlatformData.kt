package com.kappstats.model.user

import com.kappstats.Platform
import kotlinx.serialization.Serializable

@Serializable
data class PlatformData(
    override val name: Platform.PlatformType,
    override val userAgent: String
) : Platform {
    companion object {
        fun fromPlatform(value: Platform) = PlatformData(
            name = value.name,
            userAgent = value.userAgent
        )
    }
}

fun Platform.toPlatformData(): PlatformData =
    PlatformData(name = this.name, userAgent = this.userAgent)
