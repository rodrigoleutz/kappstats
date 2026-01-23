package com.kappstats.data.service.host_system_monitor

import com.kappstats.data.data_source.local.host_system_monitor.HostSystemMonitor
import com.kappstats.model.system_metrics.LinuxSystemMetrics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive

class HostSystemMonitorServiceImpl(
    private val hostSystemMonitor: HostSystemMonitor
): HostSystemMonitorService {
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val periodFlow = MutableStateFlow(1000L)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val metricsSharedFlow: SharedFlow<LinuxSystemMetrics> = periodFlow
        .flatMapLatest { period ->
            flow {
                while (currentCoroutineContext().isActive) {
                    emit(hostSystemMonitor.getInfo())
                    delay(period)
                }
            }
        }
        .shareIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            replay = 1
        )

    override suspend fun exec(value: Long): LinuxSystemMetrics? {
        setPeriod(value)
        return hostSystemMonitor.getInfo()
    }

    override fun setPeriod(value: Long) {
        periodFlow.value = value
    }

    override fun stop() {
        scope.cancel()
    }
}