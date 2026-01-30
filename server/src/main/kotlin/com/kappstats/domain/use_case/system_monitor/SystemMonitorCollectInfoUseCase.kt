package com.kappstats.domain.use_case.system_monitor

import com.kappstats.data.service.host_system_monitor.HostSystemMonitorService
import com.kappstats.model.dashboard.Dashboard
import com.kappstats.util.IdGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive

class SystemMonitorCollectInfoUseCase(
    private val hostSystemMonitorService: HostSystemMonitorService
) {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): SharedFlow<Dashboard> =
        hostSystemMonitorService.periodFlow.flatMapLatest { period ->
            flow {
                while (currentCoroutineContext().isActive) {
                    val systemMonitor = hostSystemMonitorService.getInfo()
                    systemMonitor?.let {
                        //TODO: Create save for monitoring
                        val dashboard = Dashboard(
                            id = IdGenerator.createUuid,
                            linuxSystemMetrics = systemMonitor
                        )
                        emit(dashboard)
                    }
                    delay(period)
                }
            }
        }.shareIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            replay = 1
        )

}