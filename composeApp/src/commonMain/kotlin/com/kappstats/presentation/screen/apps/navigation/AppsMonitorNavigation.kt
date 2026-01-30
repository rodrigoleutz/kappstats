package com.kappstats.presentation.screen.apps.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.kappstats.presentation.core.navigation.AppScreens
import com.kappstats.presentation.core.state.MainUiState
import com.kappstats.presentation.screen.apps.AppsMonitorScreen
import com.kappstats.presentation.screen.apps.AppsMonitorViewModel
import com.kappstats.presentation.screen.apps.screen.AppsMonitorSetScreen
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.appsMonitorNavigation(
    navBackStack: NavBackStack<NavKey>,
    mainUiState: MainUiState
) {
    entry<AppScreens.AppsMonitor> {
        val viewModel: AppsMonitorViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        AppsMonitorScreen(
            mainUiState = mainUiState,
            uiState = uiState,
            onEvent = viewModel::onEvent,
            onClickAdd = {
                navBackStack.add(AppScreens.AppsMonitor.Add)
            }
        )
    }
    entry<AppScreens.AppsMonitor.Add> {
        val viewModel: AppsMonitorViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        AppsMonitorSetScreen(
            mainUiState = mainUiState,
            uiState = uiState,
            onEvent = viewModel::onEvent
        )
    }
}