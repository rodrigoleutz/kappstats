package com.kappstats.presentation.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.kappstats.components.theme.AppDimensions
import com.kappstats.presentation.core.MainScreen
import com.kappstats.presentation.core.state.MainEvent
import com.kappstats.presentation.core.state.MainStateHolder
import com.kappstats.presentation.screen.auth.navigation.authNavigation
import com.kappstats.presentation.screen.home.HomeScreen
import com.kappstats.presentation.screen.home.HomeViewModel
import com.kappstats.presentation.screen.message.MessageScreen
import com.kappstats.presentation.screen.privacy_and_terms.PrivacyAndTermsScreen
import com.kappstats.presentation.screen.privacy_and_terms.navigation.privacyAndTermsNavigation
import com.kappstats.presentation.screen.profile.screen.ProfileProfileScreen
import com.kappstats.presentation.screen.profile.ProfileViewModel
import com.kappstats.presentation.screen.splash.SplashScreen
import com.kappstats.presentation.screen.splash.SplashViewModel
import com.kappstats.presentation.util.exitApplication
import com.kappstats.resources.Res
import com.kappstats.resources.close_application
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation() {
    val stateHolder = koinInject<MainStateHolder>()
    val uiState by stateHolder.uiState.collectAsState()
    val navBackStack: NavBackStack<NavKey> = rememberNavBackStack(
        configuration = AppScreens.configuration,
        AppScreens.Splash
    )
    var currentScreen by remember {
        mutableStateOf<AppScreens?>(null)
    }
    LaunchedEffect(stateHolder) {
        stateHolder.onBackAction = {
            navBackStack.removeLastOrNull()
        }
        stateHolder.onNavigate = { navKey ->
            navBackStack.add(navKey)
        }
    }
    LaunchedEffect(navBackStack.last()) {
        currentScreen = navBackStack.lastOrNull() as? AppScreens
        currentScreen?.let { screen ->
            stateHolder.onMainEvent(MainEvent.SetTitle(getString(screen.title)))
        }
    }
    MainScreen(
        uiState = uiState,
        onEvent = stateHolder::onMainEvent,
        selectedRoute = currentScreen ?: AppScreens.Home,
        content = {
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
                    authNavigation(navBackStack, stateHolder)
                    privacyAndTermsNavigation(navBackStack, stateHolder)
                    entry<AppScreens.Exit> {
                        MessageScreen(
                            mainUiState = uiState,
                            onConfirmClick = {
                                exitApplication()
                            },
                            onDenyClick = {
                                navBackStack.removeLastOrNull()
                            }
                        ) {
                            Text(
                                modifier = Modifier.padding(AppDimensions.ExtraExtraLarge.component),
                                text = stringResource(Res.string.close_application),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                    entry<AppScreens.Home> {
                        val viewModel: HomeViewModel = koinViewModel()
                        val userState by viewModel.stateHolder.user.collectAsState()
                        HomeScreen(
                            userState = userState,
                            onMainEvent = stateHolder::onMainEvent
                        )
                    }
                    entry<AppScreens.PrivacyAndTerms> {
                        PrivacyAndTermsScreen(paddingValues = uiState.paddingValues)
                    }
                    entry<AppScreens.Profile> {
                        val viewModel: ProfileViewModel = koinViewModel()
                        val profileUiState by viewModel.uiState.collectAsState()
                        ProfileProfileScreen(
                            mainUiState = uiState,
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
                    entry<AppScreens.Splash> {
                        val viewModel: SplashViewModel = koinViewModel()
                        SplashScreen(
                            authenticate = {
                                viewModel.authenticate { result ->
                                    val lastIndex = navBackStack.lastIndex
                                    if (lastIndex >= 0) {
                                        navBackStack[lastIndex] =
                                            if (result) AppScreens.Home else AppScreens.Auth.SignIn
                                    }
                                }
                            }
                        )
                    }
                }
            )
        }
    )
}