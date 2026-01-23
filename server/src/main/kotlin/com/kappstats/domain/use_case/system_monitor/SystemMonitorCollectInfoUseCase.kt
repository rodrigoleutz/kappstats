package com.kappstats.domain.use_case.system_monitor

import com.kappstats.data.service.host_system_monitor.HostSystemMonitorService
import com.kappstats.model.system_metrics.LinuxSystemMetrics
import kotlinx.coroutines.flow.Flow

class SystemMonitorCollectInfoUseCase(
    private val hostSystemMonitorService: HostSystemMonitorService
) {
    operator fun invoke(): Flow<LinuxSystemMetrics> {
        return hostSystemMonitorService.metricsSharedFlow
    }
}