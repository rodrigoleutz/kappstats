package com.kappstats.data.data_source.local.host_system_monitor

import com.kappstats.model.system_metrics.LinuxSystemMetrics
import kotlinx.coroutines.flow.Flow

interface HostSystemMonitor {
    fun getInfo(): LinuxSystemMetrics
}
