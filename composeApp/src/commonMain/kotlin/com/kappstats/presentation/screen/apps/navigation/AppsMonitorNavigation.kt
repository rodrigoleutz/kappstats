package com.kappstats.presentation.screen.apps.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.kappstats.presentation.core.navigation.AppScreens
import com.kappstats.presentation.core.state.MainUiState
import com.kappstats.presentation.screen.apps.AppsMonitorEvent
import com.kappstats.presentation.screen.apps.AppsMonitorListScreen
import com.kappstats.presentation.screen.apps.AppsMonitorViewModel
import com.kappstats.presentation.screen.apps.screen.AppsMonitorInfoScreen
import com.kappstats.presentation.screen.apps.screen.AppsMonitorSetScreen
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.appsMonitorNavigation(
    navBackStack: NavBackStack<NavKey>,
    mainUiState: MainUiState
) {
    entry<AppScreens.AppsMonitor> {
        val viewModel: AppsMonitorViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val appsMonitorState by viewModel.appsMonitorState.collectAsStateWithLifecycle()
        AppsMonitorListScreen(
            mainUiState = mainUiState,
            uiState = uiState,
            appsMonitorState = appsMonitorState,
            onEvent = viewModel::onEvent,
            onEdit = { id ->
                navBackStack.add(AppScreens.AppsMonitor.Edit(id))
            },
            onClickAdd = {
                navBackStack.add(AppScreens.AppsMonitor.Add)
            },
            onClick = { id ->
                navBackStack.add(AppScreens.AppsMonitor.Info(id))
            }
        )
    }
    entry<AppScreens.AppsMonitor.Add> {
        val viewModel: AppsMonitorViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        AppsMonitorSetScreen(
            mainUiState = mainUiState,
            uiState = uiState,
            onEvent = viewModel::onEvent,
            onClickSave = {
                viewModel.add { result ->
                    if(result) navBackStack.removeLastOrNull()
                }
            },
            onClickCancel = {
                viewModel.onEvent(AppsMonitorEvent.SetName(""))
                viewModel.onEvent(AppsMonitorEvent.SetDescription(""))
            }
        )
    }
    entry<AppScreens.AppsMonitor.Edit> { screen ->
        val id = screen.id
        val viewModel: AppsMonitorViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        LaunchedEffect(Unit) {
            viewModel.loadEdit(id)
        }
        AppsMonitorSetScreen(
            mainUiState = mainUiState,
            uiState = uiState,
            onEvent = viewModel::onEvent,
            id = id,
            onClickSave = {
                viewModel.update { result ->
                    if(result) navBackStack.removeLastOrNull()
                }
            },
            onClickCancel = {
                navBackStack.removeLastOrNull()
            }
        )
    }
    entry<AppScreens.AppsMonitor.Info> { screen ->
        val id = screen.id
        val viewModel: AppsMonitorViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        LaunchedEffect(Unit) {
            viewModel.loadEdit(id)
        }
        AppsMonitorInfoScreen(
            mainUiState = mainUiState,
            uiState = uiState,
            onEvent = viewModel::onEvent
        )
    }
}