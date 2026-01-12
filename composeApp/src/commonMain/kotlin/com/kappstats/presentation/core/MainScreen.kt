package com.kappstats.presentation.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.NavKey
import com.kappstats.components.navigation.ComposeRoute
import com.kappstats.components.part.widget.drawer_menu.DrawerMenuWidget
import com.kappstats.components.part.widget.drawer_menu.DrawerMenuWidgetColors
import com.kappstats.components.part.widget.top_bar.TopBarWidget
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Blue80
import com.kappstats.components.theme.Gray60
import com.kappstats.components.theme.Orange40
import com.kappstats.components.theme.Orange60
import com.kappstats.presentation.core.navigation.AppScreens
import com.kappstats.presentation.core.state.MainEvent
import com.kappstats.presentation.core.state.MainUiState
import com.kappstats.resources.Res
import com.kappstats.resources.app_name
import com.kappstats.resources.back
import com.kappstats.resources.logo
import com.kappstats.resources.menu
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.ArrowIosBack
import compose.icons.evaicons.fill.Menu2
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen(
    uiState: MainUiState,
    onEvent: (MainEvent) -> Unit,
    selectedRoute: ComposeRoute,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenuWidget(
                itemList = if (uiState.isLogged) AppScreens.logged else AppScreens.unlogged,
                selected = selectedRoute,
                colors = DrawerMenuWidgetColors(
                    containerColor = Brush.verticalGradient(
                        listOf(
                            Blue20,
                            Gray60,
                            Gray60,
                            Gray60,
                            Gray60,
                            Gray60,
                            Blue20
                        )
                    ),
                    contentColor = Blue20,
                    itemContentColor = Blue20,
                    selectedItemContainerColor = Brush.verticalGradient(
                        listOf(
                            Gray60,
                            Blue20,
                            Blue20,
                            Blue20,
                            Gray60
                        )
                    ),
                    selectedItemContentColor = Orange60
                ),
                drawerCard = {
                    Image(
                        painter = painterResource(Res.drawable.logo),
                        contentDescription = stringResource(Res.string.app_name)
                    )
                },
                onClick = { item ->
                    scope.launch {
                        onEvent(MainEvent.NavigatePush(item.route as NavKey))
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                if (uiState.hasTopBar) {
                    Column {
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
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Blue20,
                                titleContentColor = Color.White,
                                navigationIconContentColor = Orange40
                            )
                        )
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .height(AppDimensions.Medium.component)
                                .background(Brush.verticalGradient(listOf(Blue20, Gray60)))
                        ) { }
                    }
                }
            },
            containerColor = Gray60
        ) { innerPadding ->
            LaunchedEffect(innerPadding) {
                onEvent(MainEvent.SetPaddingValues(innerPadding))
            }
            content()
        }
    }
}