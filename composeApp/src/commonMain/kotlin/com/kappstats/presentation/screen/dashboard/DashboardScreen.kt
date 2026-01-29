package com.kappstats.presentation.screen.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
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
import com.kappstats.components.part.widget.dashboard.MemoryTextWidget
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Blue40
import com.kappstats.components.theme.Orange40
import com.kappstats.presentation.core.state.MainUiState
import com.kappstats.resources.Res
import com.kappstats.resources.cpu
import com.kappstats.resources.cpu_cores_logical
import com.kappstats.resources.cpu_cores_physical
import com.kappstats.resources.memory_cache
import com.kappstats.resources.memory_free
import com.kappstats.resources.memory_total
import com.kappstats.resources.memory_used
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
            MemoryTextWidget(
                modifier = Modifier.fillMaxWidth(),
                totalLabel = stringResource(Res.string.memory_total),
                totalValue = dashboard.linuxSystemMetrics.memTotal.toString(),
                freeLabel = stringResource(Res.string.memory_free),
                freeValue = dashboard.linuxSystemMetrics.memFree.toString(),
                cachedLabel = stringResource(Res.string.memory_cache),
                cachedValue = dashboard.linuxSystemMetrics.memCached.toString(),
                usedLabel = stringResource(Res.string.memory_used),
                usedValue = dashboard.linuxSystemMetrics.memUsed.toString(),
                swapFreeLabel = stringResource(Res.string.swap_free),
                swapFreeValue = (dashboard.linuxSystemMetrics.swapTotal - dashboard.linuxSystemMetrics.swapUsed).toString(),
                swapTotalLabel = stringResource(Res.string.swap_total),
                swapTotalValue = dashboard.linuxSystemMetrics.swapTotal.toString()
            )
            FlowRow {
                CpuTextWidget(
                    modifier = Modifier.weight(3f),
                    label = stringResource(Res.string.cpu),
                    value = dashboard.linuxSystemMetrics.cpuModel
                )
                FlowRow(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CpuTextWidget(
                        label = stringResource(Res.string.cpu_cores_logical),
                        value = dashboard.linuxSystemMetrics.cpuCoresLogical.toString()
                    )
                    CpuTextWidget(
                        label = stringResource(Res.string.cpu_cores_physical),
                        value = dashboard.linuxSystemMetrics.cpuCoresPhysical.toString()
                    )
                }
            }
            val lazyRowState = rememberLazyListState()

            LazyRow(
                state = lazyRowState,
                modifier = Modifier.fillMaxWidth().focusable()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Blue20.copy(0.9f),
                                Color.Transparent
                            )
                        )
                    )
            ) {
                itemsIndexed(dashboard.linuxSystemMetrics.coreUsagePercents) { index, core ->
                    CpuCoreUsageWidget(
                        modifier = Modifier,
                        label = (index + 1).toString(),
                        value = core
                    )
                }
            }
            HorizontalScrollComponent(
                modifier = Modifier.fillMaxWidth(),
                scrollState = lazyRowState
            )
        }

        Spacer(modifier = Modifier.height(mainUiState.paddingValues.calculateBottomPadding()))
    }
}