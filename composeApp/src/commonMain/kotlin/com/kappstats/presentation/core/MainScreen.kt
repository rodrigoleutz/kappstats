package com.kappstats.presentation.core

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.kappstats.components.part.widget.top_bar.TopBarWidget
import com.kappstats.presentation.core.navigation.AppNavigation
import com.kappstats.presentation.core.state.MainStateHolder
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun MainScreen(
    backStack: NavBackStack<NavKey>,
    mainStateHolder: MainStateHolder = koinInject()
) {
    val uiState by mainStateHolder.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

        }
    ) {
        Scaffold(
            topBar = {
                if (uiState.hasTopBar) {
                    TopBarWidget(
                        title = uiState.title,
                        onNavigationClick = {
                            if (uiState.isBackButton) {
                                backStack.removeLastOrNull()
                                mainStateHolder.setIsBackButton(false)
                            } else scope.launch {
                                drawerState.open()
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            LaunchedEffect(innerPadding) {
                mainStateHolder.setPaddingValues(innerPadding)
            }
            AppNavigation(backStack)
        }
    }
}