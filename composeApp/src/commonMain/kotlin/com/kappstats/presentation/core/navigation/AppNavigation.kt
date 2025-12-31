package com.kappstats.presentation.core.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.kappstats.presentation.screen.home.HomeScreen
import com.kappstats.presentation.screen.splash.SplashScreen

@Composable
fun AppNavigation(
    backStack: NavBackStack<NavKey>
) {
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        onBack = {
            backStack.removeLastOrNull()
        },
        entryProvider = entryProvider {
            entry<AppScreens.Home> { HomeScreen() }
            entry<AppScreens.Splash> { SplashScreen(backStack) }
        }
    )
}