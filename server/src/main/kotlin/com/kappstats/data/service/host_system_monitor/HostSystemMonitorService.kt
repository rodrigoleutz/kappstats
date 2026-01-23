package com.kappstats.data.service.host_system_monitor

import com.kappstats.data.service.Service
import com.kappstats.model.system_metrics.LinuxSystemMetrics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow

interface HostSystemMonitorService: Service<Long, LinuxSystemMetrics> {

    val scope: CoroutineScope
    val metricsSharedFlow: SharedFlow<LinuxSystemMetrics>
    fun setPeriod(value: Long)

    fun stop()
}