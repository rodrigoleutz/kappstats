package com.kappstats.presentation.screen.auth.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.kappstats.presentation.core.navigation.AppScreens
import com.kappstats.presentation.core.state.MainStateHolder
import com.kappstats.presentation.screen.auth.SignViewModel
import com.kappstats.presentation.screen.auth.screen.LogOutScreen
import com.kappstats.presentation.screen.auth.screen.SignInScreen
import com.kappstats.presentation.screen.auth.screen.SignUpScreen
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.authNavigation(
    navBackStack: NavBackStack<NavKey>,
    stateHolder: MainStateHolder
) {
    entry<AppScreens.Auth.LogOut> {
        val viewModel: SignViewModel = koinViewModel()
        LogOutScreen(
            cancel = {
                navBackStack.removeLastOrNull()
            },
            logOut = {
                viewModel.logOut { result ->
                    if(result) {
                        navBackStack.clear()
                        navBackStack.add(AppScreens.Auth.SignIn)
                    }
                }
            }
        )
    }
    entry<AppScreens.Auth.SignIn> {
        val mainUiState by stateHolder.uiState.collectAsState()
        val viewModel: SignViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsState()
        SignInScreen(
            paddingValues = mainUiState.paddingValues,
            uiState = uiState,
            onMainEvent = stateHolder::onMainEvent,
            onEvent = viewModel::onEvent,
            onSignIn = {
                viewModel.signIn { result ->
                    if (result) {
                        navBackStack.clear()
                        navBackStack.add(AppScreens.Home)
                    }
                }
            }
        )
    }
    entry<AppScreens.Auth.SignUp> {
        val mainUiState by stateHolder.uiState.collectAsState()
        val viewModel: SignViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsState()
        SignUpScreen(
            paddingValues = mainUiState.paddingValues,
            uiState = uiState,
            onEvent = viewModel::onEvent,
            onMainEvent = stateHolder::onMainEvent,
            onSignUp = {
                viewModel.signUp { result ->
                    if(result) {
                        navBackStack.clear()
                        navBackStack.add(AppScreens.Home)
                    }
                }
            }
        )
    }
}