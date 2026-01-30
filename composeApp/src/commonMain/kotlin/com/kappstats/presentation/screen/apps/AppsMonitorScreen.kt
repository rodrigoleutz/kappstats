package com.kappstats.presentation.screen.apps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import com.kappstats.components.part.component.card.CardMessageComponent
import com.kappstats.components.theme.AppDimensions
import com.kappstats.domain.data_state.apps_monitor.AppsMonitorState
import com.kappstats.presentation.core.state.MainUiState
import com.kappstats.resources.Res
import com.kappstats.resources.add
import com.kappstats.resources.empty_list
import compose.icons.EvaIcons
import compose.icons.TablerIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Plus
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppsMonitorScreen(
    mainUiState: MainUiState,
    uiState: AppsMonitorUiState,
    appsMonitorState: AppsMonitorState,
    onEvent: (AppsMonitorEvent) -> Unit,
    onClickAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (appsMonitorState.mapAppsMonitor.values.toList().isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CardMessageComponent(
                    message = stringResource(Res.string.empty_list)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                contentPadding = mainUiState.paddingValues
            ) {
                items(appsMonitorState.mapAppsMonitor.values.toList()) { item ->
                    Text(text = item.toString())
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(
                    bottom = mainUiState.paddingValues.calculateBottomPadding() + AppDimensions.Large.component,
                    end = mainUiState.paddingValues.calculateEndPadding(LayoutDirection.Ltr) + AppDimensions.Large.component
                ),
            onClick = {
                onClickAdd()
            }
        ) {
            Icon(
                imageVector = EvaIcons.Fill.Plus,
                contentDescription = stringResource(Res.string.add)
            )
        }
    }
}