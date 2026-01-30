package com.kappstats.presentation.screen.apps.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kappstats.presentation.core.state.MainUiState
import com.kappstats.presentation.screen.apps.AppsMonitorEvent
import com.kappstats.presentation.screen.apps.AppsMonitorUiState

@Composable
fun AppsMonitorSetScreen(
    mainUiState: MainUiState,
    uiState: AppsMonitorUiState,
    onEvent: (AppsMonitorEvent) -> Unit,
    id: String? = null,
    modifier: Modifier = Modifier
) {

}