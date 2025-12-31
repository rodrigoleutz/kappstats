package com.kappstats

import kotlinx.browser.window

class JsPlatform: Platform {
    override val name: Platform.PlatformType = Platform.PlatformType.WebJs
    override val userAgent: String
        get() = window.navigator.userAgent
}

actual fun getPlatform(): Platform = JsPlatform()