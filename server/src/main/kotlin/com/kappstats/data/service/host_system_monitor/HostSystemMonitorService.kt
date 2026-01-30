package com.kappstats.data.service.host_system_monitor

import com.kappstats.data.service.Service
import com.kappstats.model.system_metrics.LinuxSystemMetrics
import kotlinx.coroutines.flow.MutableStateFlow

interface HostSystemMonitorService: Service<Long, LinuxSystemMetrics> {

    val periodFlow: MutableStateFlow<Long>
    suspend fun getInfo(): LinuxSystemMetrics?
    fun setPeriod(value: Long)
}