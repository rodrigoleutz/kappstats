package com.kappstats

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: Platform.PlatformType  = Platform.PlatformType.MobileIOS
    override val userAgent: String
        get() = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()