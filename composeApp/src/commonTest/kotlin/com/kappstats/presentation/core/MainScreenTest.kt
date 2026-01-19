package com.kappstats.presentation.core

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.swipeLeft
import com.kappstats.presentation.constants.Tags
import com.kappstats.presentation.core.navigation.AppScreens
import com.kappstats.presentation.core.state.MainEvent
import com.kappstats.presentation.core.state.MainStateHolder
import com.kappstats.util.BaseComposeIntegrationTest
import org.koin.compose.koinInject
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class MainScreenTest : BaseComposeIntegrationTest() {

    @Test
    fun `Top bar and set title`() = runComposeUiTest {
        var stateHolder: MainStateHolder? = null
        var drawerState: DrawerState? = null
        setContent {
            drawerState = rememberDrawerState(DrawerValue.Closed)
            stateHolder = koinInject<MainStateHolder>()
            val uiState by stateHolder.uiState.collectAsState()
            MainScreen(
                drawerState = drawerState,
                uiState = uiState,
                onEvent = stateHolder::onMainEvent,
                selectedRoute = AppScreens.Auth.SignIn,
                onBottomBarClick = { _ ->
                    
                },
                content = {}
            )
        }
        val topBar = onNodeWithTag(Tags.TOP_BAR)
        topBar.assertDoesNotExist()
        stateHolder?.onMainEvent(MainEvent.SetHasTopBar(true))
        topBar.isDisplayed()
        topBar.assertExists()
        val modalDrawer = onNodeWithTag(Tags.MODAL_DRAWER)
        modalDrawer.assertExists()
        val drawerMenu = onNodeWithTag(Tags.DRAWER_MENU)
        drawerMenu.assertExists()
        drawerMenu.assertIsNotDisplayed()
        val navButton = onNodeWithTag(Tags.NAV_BUTTON)
        navButton.assertExists()
        navButton.performClick()
        drawerMenu.assertIsDisplayed()
        drawerMenu.performTouchInput { swipeLeft() }
        drawerMenu.assertIsNotDisplayed()
        navButton.performClick()
        drawerMenu.assertIsDisplayed()
        modalDrawer.performTouchInput { click(percentOffset(0.9f, 0.5f)) }
        waitForIdle()
        assertEquals(true, drawerState?.isClosed)
        drawerMenu.assertIsNotDisplayed()
    }
}