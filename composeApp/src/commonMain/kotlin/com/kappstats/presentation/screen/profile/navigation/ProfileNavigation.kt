package com.kappstats.presentation.screen.profile.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigationevent.OnBackCompletedFallback
import com.kappstats.presentation.core.navigation.AppScreens
import com.kappstats.presentation.core.state.MainStateHolder
import com.kappstats.presentation.screen.profile.ProfileViewModel
import com.kappstats.presentation.screen.profile.screen.ProfileAuthScreen
import com.kappstats.presentation.screen.profile.screen.ProfileProfileScreen
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.profileNavigation(
    navBackStack: NavBackStack<NavKey>,
    stateHolder: MainStateHolder
) {
    entry<AppScreens.Profile.ProfileProfile> {
        val viewModel: ProfileViewModel = koinViewModel()
        val profileUiState by viewModel.uiState.collectAsState()
        val mainUiState by stateHolder.uiState.collectAsState()
        ProfileProfileScreen(
            mainUiState = mainUiState,
            uiState = profileUiState,
            onEvent = viewModel::onEvent,
            enableUpdate = viewModel.enabledUpdate,
            onUpdate = {
                viewModel.updateProfile { result ->
                    if (result) navBackStack.removeLastOrNull()
                }
            },
            onCancel = {
                navBackStack.removeLastOrNull()
            }
        )
    }
    entry<AppScreens.Profile.ProfileAuth> {
        val viewModel: ProfileViewModel = koinViewModel()
        val profileUiState by viewModel.uiState.collectAsState()
        val mainUiState by stateHolder.uiState.collectAsState()
        ProfileAuthScreen(
            mainUiState = mainUiState,
            uiState = profileUiState,
            onEvent = viewModel::onEvent,
            updateEnabled = viewModel.authEnableUpdate,
            onSave = {

            },
            onCancel = {
                navBackStack.removeLastOrNull()
            }
        )
    }

}