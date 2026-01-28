package com.kappstats.presentation.screen.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import com.kappstats.components.part.widget.dashboard.CpuCoreUsageWidget
import com.kappstats.components.part.widget.dashboard.CpuTextWidget
import com.kappstats.components.part.widget.dashboard.MemoryTextWidget
import com.kappstats.components.theme.AppDimensions
import com.kappstats.presentation.core.state.MainUiState
import com.kappstats.resources.Res
import com.kappstats.resources.cpu
import com.kappstats.resources.cpu_cores_logical
import com.kappstats.resources.cpu_cores_physical
import com.kappstats.resources.memory_cache
import com.kappstats.resources.memory_free
import com.kappstats.resources.memory_total
import com.kappstats.resources.memory_used
import org.jetbrains.compose.resources.stringResource

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
            MemoryTextWidget(
                modifier = Modifier.fillMaxWidth(),
                totalLabel = stringResource(Res.string.memory_total),
                totalValue = dashboard.linuxSystemMetrics.memTotal.toString(),
                freeLabel = stringResource(Res.string.memory_free),
                freeValue = dashboard.linuxSystemMetrics.memFree.toString(),
                cachedLabel = stringResource(Res.string.memory_cache),
                cachedValue = dashboard.linuxSystemMetrics.memCached.toString(),
                usedLabel = stringResource(Res.string.memory_used),
                usedValue = dashboard.linuxSystemMetrics.memUsed.toString()
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                CpuTextWidget(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.cpu),
                    value = dashboard.linuxSystemMetrics.cpuModel
                )
                CpuTextWidget(
                    label = stringResource(Res.string.cpu_cores_logical),
                    value = dashboard.linuxSystemMetrics.cpuCoresLogical.toString()
                )
                CpuTextWidget(
                    label = stringResource(Res.string.cpu_cores_physical),
                    value = dashboard.linuxSystemMetrics.cpuCoresPhysical.toString()
                )
            }
            FlowRow(
                maxItemsInEachRow = (totalWidth/ AppDimensions.Medium.image).toInt()
            ) {
                dashboard.linuxSystemMetrics.coreUsagePercents.forEachIndexed { index, core ->
                    CpuCoreUsageWidget(
                        modifier = Modifier,
                        label = (index+1).toString(),
                        value = core
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(mainUiState.paddingValues.calculateBottomPadding()))
    }
}