package com.kappstats

import com.kappstats.model.user.PlatformData


interface Platform {
    val name: PlatformType
    val userAgent: String

    enum class PlatformType {
        Desktop,
        Jvm,
        MobileAndroid,
        MobileIOS,
        WebJs,
        WebWasm;
    }
}

expect fun getPlatform(): Platform