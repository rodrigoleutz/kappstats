package com.kappstats.data.service.host_system_monitor

import com.kappstats.data.data_source.local.host_system_monitor.HostSystemMonitor
import com.kappstats.model.system_metrics.LinuxSystemMetrics
import kotlinx.coroutines.flow.MutableStateFlow

class HostSystemMonitorServiceImpl(
    private val hostSystemMonitor: HostSystemMonitor
): HostSystemMonitorService {

    override val periodFlow = MutableStateFlow(3_000L)

    override suspend fun getInfo(): LinuxSystemMetrics {
        return hostSystemMonitor.getInfo()
    }

    override suspend fun exec(value: Long): LinuxSystemMetrics? {
        setPeriod(value)
        return hostSystemMonitor.getInfo()
    }

    override fun setPeriod(value: Long) {
        periodFlow.value = value
    }

}