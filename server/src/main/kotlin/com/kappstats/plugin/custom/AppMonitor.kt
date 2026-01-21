package com.kappstats.plugin.custom

import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.BaseApplicationPlugin
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.util.AttributeKey

class AppMonitor(configuration: Configuration) {
    val onReceived = configuration.onReceived
    val headerName = configuration.headerName

    class Configuration {
        var headerName = "X-AppMonitor"
        var onReceived: suspend (String) -> Unit = {}
    }

    companion object Plugin :
        BaseApplicationPlugin<ApplicationCallPipeline, Configuration, AppMonitor> {
        override val key: AttributeKey<AppMonitor> = AttributeKey("AppMonitor")
        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: Configuration.() -> Unit
        ): AppMonitor {
            val configuration = Configuration().apply(configure)
            val plugin = AppMonitor(configuration)
            pipeline.intercept(ApplicationCallPipeline.Monitoring) {
                val appMonitorHeader = call.request.header(plugin.headerName)
                    ?: return@intercept
                plugin.onReceived(appMonitorHeader)
            }
            return plugin
        }

    }
}