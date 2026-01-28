package com.kappstats.model.dashboard

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.model.system_metrics.LinuxSystemMetrics
import kotlinx.serialization.Serializable

@Serializable
data class Dashboard(
    val linuxSystemMetrics: LinuxSystemMetrics,
    val currentDate: AppDateTime = AppDateTime.now
)
