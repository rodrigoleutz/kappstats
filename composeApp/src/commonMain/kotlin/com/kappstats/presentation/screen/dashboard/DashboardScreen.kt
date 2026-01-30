package com.kappstats.presentation.screen.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.foundation.layout.FlowRowOverflowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import com.kappstats.components.part.component.container.ScrollableContainerComponent
import com.kappstats.components.part.component.scroll.HorizontalScrollComponent
import com.kappstats.components.part.widget.dashboard.CpuCoreUsageWidget
import com.kappstats.components.part.widget.dashboard.CpuTextWidget
import com.kappstats.components.part.widget.dashboard.HostInfoWidget
import com.kappstats.components.part.widget.dashboard.MemoryTextWidget
import com.kappstats.components.part.widget.dashboard.NetworkStatsWidget
import com.kappstats.components.part.widget.dashboard.ProcessesWidget
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Blue40
import com.kappstats.components.theme.Green20
import com.kappstats.components.theme.Orange20
import com.kappstats.components.theme.Orange40
import com.kappstats.components.theme.Orange80
import com.kappstats.presentation.core.state.MainUiState
import com.kappstats.presentation.util.fromBytesToGigabyteString
import com.kappstats.presentation.util.fromMbToGbString
import com.kappstats.presentation.util.secondsToMinString
import com.kappstats.resources.Res
import com.kappstats.resources.cpu
import com.kappstats.resources.cpu_cores_logical
import com.kappstats.resources.cpu_cores_physical
import com.kappstats.resources.memory_cache
import com.kappstats.resources.memory_free
import com.kappstats.resources.memory_total
import com.kappstats.resources.memory_used
import com.kappstats.resources.processes
import com.kappstats.resources.swap_free
import com.kappstats.resources.swap_total
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
    mainUiState: MainUiState,
    uiState: DashboardUiState,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val totalWidth = windowInfo.containerDpSize.width
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(mainUiState.paddingValues.calculateTopPadding()))

        uiState.dashboard?.let { dashboard ->
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CpuCoreUsageWidget(
                    modifier = Modifier,
                    label = stringResource(Res.string.cpu),
                    value = dashboard.linuxSystemMetrics.cpuUsagePercent,
                    cardRadialGradient = listOf(Blue40, Blue40, Blue20, Orange20)
                )
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    HostInfoWidget(
                        hostname = dashboard.linuxSystemMetrics.hostname,
                        kernelVersion = dashboard.linuxSystemMetrics.kernelVersion,
                        uptime = dashboard.linuxSystemMetrics.uptimeSeconds.secondsToMinString()
                    )
                    CpuTextWidget(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(Res.string.cpu),
                        value = dashboard.linuxSystemMetrics.cpuModel
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CpuTextWidget(
                            modifier = Modifier.weight(1f),
                            label = stringResource(Res.string.cpu_cores_logical),
                            value = dashboard.linuxSystemMetrics.cpuCoresLogical.toString()
                        )
                        CpuTextWidget(
                            modifier = Modifier.weight(1f),
                            label = stringResource(Res.string.cpu_cores_physical),
                            value = dashboard.linuxSystemMetrics.cpuCoresPhysical.toString()
                        )
                    }
                    MemoryTextWidget(
                        modifier = Modifier.fillMaxWidth(),
                        totalLabel = stringResource(Res.string.memory_total),
                        totalValue = dashboard.linuxSystemMetrics.memTotal.fromMbToGbString(),
                        freeLabel = stringResource(Res.string.memory_free),
                        freeValue = dashboard.linuxSystemMetrics.memFree.fromMbToGbString(),
                        cachedLabel = stringResource(Res.string.memory_cache),
                        cachedValue = dashboard.linuxSystemMetrics.memCached.fromMbToGbString(),
                        usedLabel = stringResource(Res.string.memory_used),
                        usedValue = dashboard.linuxSystemMetrics.memUsed.fromMbToGbString(),
                        swapFreeLabel = stringResource(Res.string.swap_free),
                        swapFreeValue = (dashboard.linuxSystemMetrics.swapTotal - dashboard.linuxSystemMetrics.swapUsed).fromMbToGbString(),
                        swapTotalLabel = stringResource(Res.string.swap_total),
                        swapTotalValue = dashboard.linuxSystemMetrics.swapTotal.fromMbToGbString()
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.Bottom
            ) {
                val lazyRowState = rememberLazyListState()
                LazyRow(
                    state = lazyRowState,
                    modifier = Modifier.fillMaxWidth().focusable()
                ) {
                    itemsIndexed(dashboard.linuxSystemMetrics.coreUsagePercents) { index, core ->
                        CpuCoreUsageWidget(
                            modifier = Modifier,
                            label = (index + 1).toString(),
                            value = core,
                            gradientSize = AppDimensions.Medium.image,
                            cardRadialGradient = listOf(Blue40, Blue40, Blue20, Green20)
                        )
                    }
                }
                HorizontalScrollComponent(
                    modifier = Modifier.fillMaxWidth(),
                    scrollState = lazyRowState
                )
            }
            ProcessesWidget(
                label = stringResource(Res.string.processes),
                current = dashboard.linuxSystemMetrics.runningProcesses.toLong(),
                total = dashboard.linuxSystemMetrics.totalProcesses.toLong()
            )
            Column {
                dashboard.linuxSystemMetrics.interfaces.forEach { network ->
                    NetworkStatsWidget(
                        modifier = Modifier,
                        label = network.name,
                        rxBytes = network.rxBytes.fromBytesToGigabyteString(),
                        txBytes = network.txBytes.fromBytesToGigabyteString(),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(mainUiState.paddingValues.calculateBottomPadding()))
    }
}