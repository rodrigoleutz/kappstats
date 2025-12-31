package com.kappstats

class WasmPlatform: Platform {
    override val name: Platform.PlatformType = Platform.PlatformType.WebWasm
    @OptIn(ExperimentalWasmJsInterop::class)
    override val userAgent: String
        get() = js("window.navigator.userAgent")
}

actual fun getPlatform(): Platform = WasmPlatform()