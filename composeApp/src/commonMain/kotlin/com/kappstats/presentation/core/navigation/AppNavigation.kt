package com.kappstats.presentation.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.kappstats.presentation.core.state.MainEvent
import com.kappstats.presentation.core.state.MainStateHolder
import com.kappstats.presentation.screen.auth.navigation.authNavigation
import com.kappstats.presentation.screen.home.HomeScreen
import com.kappstats.presentation.screen.home.HomeViewModel
import com.kappstats.presentation.screen.splash.SplashScreen
import com.kappstats.presentation.screen.splash.SplashViewModel
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(
    stateHolder: MainStateHolder,
) {
    val navBackStack: NavBackStack<NavKey> = rememberNavBackStack(
        configuration = AppScreens.configuration,
        AppScreens.Splash
    )
    LaunchedEffect(stateHolder) {
        stateHolder.onBackAction = {
            navBackStack.removeLastOrNull()
        }
        stateHolder.onNavigate = { navKey ->
            navBackStack.add(navKey)
        }
    }
    LaunchedEffect(navBackStack.last()) {
        val currentScreen = navBackStack.lastOrNull() as? AppScreens
        currentScreen?.let { screen ->
            stateHolder.onMainEvent(MainEvent.SetTitle(getString(screen.title)))
        }
    }
    NavDisplay(
        backStack = navBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        onBack = {
            navBackStack.removeLastOrNull()
        },
        entryProvider = entryProvider {
            authNavigation(navBackStack)
            entry<AppScreens.Home> {
                val viewModel: HomeViewModel = koinViewModel()
                HomeScreen(
                    onMainEvent = stateHolder::onMainEvent
                )
            }
            entry<AppScreens.Splash> {
                val viewModel: SplashViewModel = koinViewModel()
                viewModel.authenticate { result ->
                    val lastIndex = navBackStack.lastIndex
                    if(lastIndex >= 0) {
                        navBackStack[lastIndex] =
                            if (result) AppScreens.Home else AppScreens.Auth.SignIn
                    }
                }
                SplashScreen()
            }
        }
    )
}