package com.kappstats.presentation.core

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.navigation3.runtime.NavKey
import com.kappstats.components.navigation.ComposeRoute
import com.kappstats.components.part.widget.bottom_bar.BottomNavigationBarWidget
import com.kappstats.components.part.widget.drawer_menu.DrawerMenuWidget
import com.kappstats.components.part.widget.drawer_menu.DrawerMenuWidgetColors
import com.kappstats.components.part.widget.loading.LoadingScreenWidget
import com.kappstats.components.part.widget.snackbar.AppSnackbarWidget
import com.kappstats.components.part.widget.top_bar.TopBarWidget
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Gray60
import com.kappstats.components.theme.Orange40
import com.kappstats.components.theme.Orange60
import com.kappstats.components.theme.Orange80
import com.kappstats.presentation.constants.Tags
import com.kappstats.presentation.core.navigation.AppScreens
import com.kappstats.presentation.core.state.MainEvent
import com.kappstats.presentation.core.state.MainUiState
import com.kappstats.resources.Res
import com.kappstats.resources.app_name
import com.kappstats.resources.back
import com.kappstats.resources.close
import com.kappstats.resources.loading
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
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    onBottomBarClick: (ComposeRoute) -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        modifier = Modifier.testTag(Tags.MODAL_DRAWER),
        drawerState = drawerState,
        drawerContent = {
            DrawerMenuWidget(
                modifier = Modifier.testTag(Tags.DRAWER_MENU),
                itemList = if (uiState.isLogged) AppScreens.logged else AppScreens.unlogged,
                selected = selectedRoute,
                dividerBefore = if (uiState.isLogged) AppScreens.loggedDrawerDivider
                else AppScreens.unloggedDrawerDivider,
                colors = DrawerMenuWidgetColors(
                    containerColor = Brush.verticalGradient(listOf(Gray60, Gray60)),
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
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .background(Brush.verticalGradient(listOf(Blue20, Gray60)))
                            .padding(AppDimensions.Large.component),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.logo),
                            contentDescription = stringResource(Res.string.app_name)
                        )
                    }
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
            modifier = Modifier.testTag(Tags.SCAFFOLD),
            topBar = {
                if (uiState.hasTopBar) {
                    Column {
                        TopBarWidget(
                            modifier = Modifier.testTag(Tags.TOP_BAR),
                            modifierNavIcon = Modifier.testTag(Tags.NAV_BUTTON),
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
                                containerColor = Blue20.copy(0.9f),
                                titleContentColor = Color.White,
                                navigationIconContentColor = Orange40
                            )
                        )
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .height(AppDimensions.Medium.component)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Blue20.copy(0.9f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        ) { }
                    }
                }
            },
            bottomBar = {
                selectedRoute.bottomBar?.let { bottomBar ->
                    AnimatedVisibility(bottomBar.isNotEmpty()) {
                        Column {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                                    .height(AppDimensions.Medium.component)
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color.Transparent,
                                                Blue20.copy(0.9f)
                                            )
                                        )
                                    )
                            ) { }
                            BottomNavigationBarWidget(
                                items = bottomBar,
                                selected = selectedRoute,
                                onClick = { item ->
                                    onBottomBarClick(item)
                                },
                                containerColor = Blue20.copy(0.9f),
                                contentColor = Orange40,
                                itemColors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Orange40,
                                    selectedTextColor = Orange40,
                                    unselectedIconColor = Orange60,
                                    unselectedTextColor = Orange60,
                                    indicatorColor = Orange80.copy(0.2f)
                                )
                            )
                        }
                    }
                }
            },
            snackbarHost = {
                AppSnackbarWidget(
                    uiState.snackbarHostState,
                    stringResource(Res.string.close),
                    modifier = Modifier.testTag(Tags.SNACKBAR)
                )
            },
            containerColor = Gray60
        ) { innerPadding ->
            LaunchedEffect(innerPadding) {
                onEvent(MainEvent.SetPaddingValues(innerPadding))
            }
            content()
            AnimatedVisibility(uiState.isLoading, enter = fadeIn(), exit = fadeOut()) {
                LoadingScreenWidget(
                    loadingText = stringResource(Res.string.loading),
                    modifier = Modifier.testTag(Tags.LOADING)
                )
            }
        }
    }
}