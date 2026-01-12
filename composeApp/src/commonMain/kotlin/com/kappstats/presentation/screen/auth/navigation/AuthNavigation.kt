package com.kappstats.presentation.screen.auth.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.kappstats.presentation.core.navigation.AppScreens
import com.kappstats.presentation.core.state.MainStateHolder
import com.kappstats.presentation.screen.auth.SignViewModel
import com.kappstats.presentation.screen.auth.screen.SignInScreen
import com.kappstats.presentation.screen.auth.screen.SignUpScreen
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.authNavigation(
    navBackStack: NavBackStack<NavKey>,
    stateHolder: MainStateHolder
) {
    entry<AppScreens.Auth.SignIn> {
        val viewModel: SignViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsState()
        SignInScreen(
            uiState = uiState,
            onMainEvent = stateHolder::onMainEvent,
            onEvent = viewModel::onEvent,
            onSignIn = {
                viewModel.signIn { result ->
                    if (result) navBackStack.add(AppScreens.Home)
                }
            }
        )
    }
    entry<AppScreens.Auth.SignUp> {
        SignUpScreen()
    }
}