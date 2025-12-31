package com.kappstats

class JVMPlatform: Platform {
    private fun getDeviceInfo(): String {
        val os = System.getProperty("os.name").trim()
        val osVersion = System.getProperty("os.version").trim()
        val javaVersion = System.getProperty("java.version").trim()
        return "$os $osVersion (Java $javaVersion)"
    }

    override val name: Platform.PlatformType = Platform.PlatformType.Jvm
    override val userAgent: String
        get() = getDeviceInfo()
}

actual fun getPlatform(): Platform = JVMPlatform()