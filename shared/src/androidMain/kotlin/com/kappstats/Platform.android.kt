package com.kappstats

import android.os.Build

class AndroidPlatform : Platform {
    override val name: Platform.PlatformType = Platform.PlatformType.MobileAndroid
    override val userAgent: String
        get() {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            val version = Build.VERSION.RELEASE
            return "$manufacturer $model (Android $version)"
        }
}

actual fun getPlatform(): Platform = AndroidPlatform()