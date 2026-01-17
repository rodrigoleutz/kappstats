package com.kappstats.presentation.screen.auth.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.kappstats.components.theme.AppDimensions
import com.kappstats.presentation.core.navigation.AppScreens
import com.kappstats.presentation.core.state.MainStateHolder
import com.kappstats.presentation.screen.auth.SignViewModel
import com.kappstats.presentation.screen.auth.screen.SignInScreen
import com.kappstats.presentation.screen.auth.screen.SignUpScreen
import com.kappstats.presentation.screen.message.MessageScreen
import com.kappstats.resources.Res
import com.kappstats.resources.logout
import com.kappstats.resources.logout_message
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.LogOut
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.authNavigation(
    navBackStack: NavBackStack<NavKey>,
    stateHolder: MainStateHolder
) {
    entry<AppScreens.Auth.LogOut> {
        val viewModel: SignViewModel = koinViewModel()
        val mainUiState by viewModel.stateHolder.uiState.collectAsState()
        MessageScreen(
            mainUiState = mainUiState,
            confirmIcon = EvaIcons.Fill.LogOut,
            confirmLabel = stringResource(Res.string.logout),
            onDenyClick = {
                navBackStack.removeLastOrNull()
            },
            onConfirmClick = {
                viewModel.logOut { result ->
                    if(result) {
                        navBackStack.clear()
                        navBackStack.add(AppScreens.Auth.SignIn)
                    }
                }
            }
        ) {
            Text(
                modifier = Modifier.padding(AppDimensions.ExtraExtraLarge.component),
                text = stringResource(Res.string.logout_message),
                style = MaterialTheme.typography.titleLarge
            )
        }
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