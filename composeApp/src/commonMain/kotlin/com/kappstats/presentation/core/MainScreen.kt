package com.kappstats.presentation.core

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.kappstats.components.part.widget.top_bar.TopBarWidget
import com.kappstats.presentation.core.state.MainEvent
import com.kappstats.presentation.core.state.MainUiState
import com.kappstats.resources.Res
import com.kappstats.resources.back
import com.kappstats.resources.menu
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.ArrowIosBack
import compose.icons.evaicons.fill.Menu2
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MainScreen(
    uiState: MainUiState,
    onEvent: (MainEvent) -> Unit,
    content: @Composable () -> Unit
) {
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
                        navigationIcon = if (uiState.isBackButton) EvaIcons.Fill.ArrowIosBack
                        else EvaIcons.Fill.Menu2,
                        navigationDescription = stringResource(
                            if (uiState.isBackButton) Res.string.back else Res.string.menu
                        ),
                        onNavigationClick = {
                            if (uiState.isBackButton) {
                                onEvent(MainEvent.SetIsBackButton(false))
                            } else scope.launch {
                                drawerState.open()
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            LaunchedEffect(innerPadding) {
                onEvent(MainEvent.SetPaddingValues(innerPadding))
            }
            content()
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        uiState = MainUiState(
            hasTopBar = true,
            title = "Test title"
        ),
        onEvent = {},
        content = {}
    )

}