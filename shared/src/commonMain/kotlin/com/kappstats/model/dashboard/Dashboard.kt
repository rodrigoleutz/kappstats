package com.kappstats.model.dashboard

import com.kappstats.model.system_metrics.LinuxSystemMetrics
import kotlinx.serialization.Serializable

@Serializable
data class Dashboard(
    val linuxSystemMetrics: LinuxSystemMetrics
)
