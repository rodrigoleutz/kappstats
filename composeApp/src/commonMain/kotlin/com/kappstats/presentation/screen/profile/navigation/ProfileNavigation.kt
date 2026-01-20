package com.kappstats.presentation.screen.profile.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
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
        val profileUiState by viewModel.uiState.collectAsStateWithLifecycle()
        val mainUiState by stateHolder.uiState.collectAsStateWithLifecycle()
        val userState by stateHolder.user.collectAsStateWithLifecycle()
        val enableUpdate = remember(userState, profileUiState) {
            viewModel.enabledUpdateProfile
        }
        ProfileProfileScreen(
            mainUiState = mainUiState,
            uiState = profileUiState,
            onEvent = viewModel::onEvent,
            enableUpdate = enableUpdate,
            onUpdate = {
                viewModel.updateProfile()
            },
            onCancel = {
                navBackStack.removeLastOrNull()
            }
        )
    }
    entry<AppScreens.Profile.ProfileAuth> {
        val viewModel: ProfileViewModel = koinViewModel()
        val profileUiState by viewModel.uiState.collectAsStateWithLifecycle()
        val mainUiState by stateHolder.uiState.collectAsStateWithLifecycle()
        val userState by stateHolder.user.collectAsStateWithLifecycle()
        val enableUpdate = remember(userState, profileUiState) {
            viewModel.enableUpdateAuth
        }
        ProfileAuthScreen(
            mainUiState = mainUiState,
            uiState = profileUiState,
            onEvent = viewModel::onEvent,
            updateEnabled = enableUpdate,
            onSave = {
                viewModel.updateAuth()
            },
            onCancel = {
                navBackStack.removeLastOrNull()
            }
        )
    }

}