package com.kappstats.data.local.system

import com.kappstats.model.system_metrics.LinuxSystemMetrics
import kotlinx.coroutines.flow.Flow

interface HostSystemMonitor {
    fun collect(period: Long = 2L): Flow<LinuxSystemMetrics>
}
