package com.kappstats.presentation.screen.auth

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.runComposeUiTest
import com.kappstats.di.dataModule
import com.kappstats.di.domainModule
import com.kappstats.di.presentationModule
import com.kappstats.presentation.screen.auth.screen.SignInScreen
import com.kappstats.presentation.screen.auth.screen.SignUpScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class SignScreensTest {

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(dataModule, domainModule, presentationModule)
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `Test Sign In Screen`() = runComposeUiTest {
        var viewModel: SignViewModel? = null
        var signInClicked: Boolean = false
        setContent {
            viewModel = koinViewModel()
            val mainState by viewModel.stateHolder.uiState.collectAsState()
            val uiState by viewModel.uiState.collectAsState()
            SignInScreen(
                paddingValues = mainState.paddingValues,
                uiState = uiState,
                onEvent = viewModel::onEvent,
                onMainEvent = viewModel.stateHolder::onMainEvent,
                onSignIn = {
                    signInClicked = true
                }
            )
        }

        onNodeWithText("Email").assertExists()
        onNodeWithText("Password").assertExists()
        onNodeWithTag("email_input").assertExists()
        onNodeWithTag("password_input").assertExists()
        onNodeWithTag("clear_button").assertExists()
        onNodeWithTag("login_button").assertExists()
        onNodeWithTag("login_button").assertIsNotEnabled()
        with(onNodeWithTag("email_input")) {
            performClick()
            performTextReplacement("test@test.com")
        }
        waitForIdle()
        assertEquals("test@test.com", viewModel?.uiState?.value?.email)
        with(onNodeWithTag("password_input")) {
            performClick()
            performTextReplacement("Password#123")
        }
        waitForIdle()
        assertEquals("Password#123", viewModel?.uiState?.value?.password)
        onNodeWithTag("login_button").performClick()
        assertEquals(true, signInClicked)
    }


    @Test
    fun `Test sign up screen`() = runComposeUiTest {
        var viewModel: SignViewModel? = null
        var signUpClick = false
        var hasUsername: Boolean? = null
        setContent {
            viewModel = koinViewModel()
            val mainState by viewModel.stateHolder.uiState.collectAsState()
            val uiState by viewModel.uiState.collectAsState()
            SignUpScreen(
                paddingValues = mainState.paddingValues,
                uiState = uiState,
                onMainEvent = viewModel.stateHolder::onMainEvent,
                onEvent = viewModel::onEvent,
                hasUsername = hasUsername,
                onSignUp = {
                    signUpClick = true
                }
            )
        }

        val nameNode = onNodeWithTag("name_input")
        nameNode.assertExists()
        val emailNode = onNodeWithTag("email_input")
        emailNode.assertExists()
        val usernameNode = onNodeWithTag("username_input")
        usernameNode.assertExists()
        val passwordNode = onNodeWithTag("password_input")
        passwordNode.assertExists()
        val passwordConfirmNode = onNodeWithTag("password_confirm_input")
        passwordConfirmNode.assertExists()
        val registerButtonNode = onNodeWithTag("register_button")
        registerButtonNode.assertExists()
        val clearButtonNode = onNodeWithTag("clear_button")
        clearButtonNode.assertExists()
        registerButtonNode.assertIsNotEnabled()

        nameNode.performTextInput("Test Name")
        assertEquals("Test Name", viewModel?.uiState?.value?.name)
        with(emailNode) {
            performClick()
            performTextInput("test@test.com")
        }
        waitForIdle()
        assertEquals("test@test.com", viewModel?.uiState?.value?.email)
        with(usernameNode) {
            performClick()
            performTextInput("test123")
        }
        hasUsername = false
        waitForIdle()
        assertEquals("test123", viewModel?.uiState?.value?.username)
        with(passwordNode) {
            performClick()
            performTextInput("Password#123")
        }
        waitForIdle()
        assertEquals("Password#123", viewModel?.uiState?.value?.password)
        with(passwordConfirmNode) {
            performClick()
            performTextInput("Password#1")
        }
        waitForIdle()
        registerButtonNode.assertIsNotEnabled()
        with(passwordConfirmNode) {
            performClick()
            performTextInput("23")
        }
        waitForIdle()

        registerButtonNode.assertIsEnabled()
        registerButtonNode.performClick()
        assertEquals(true, signUpClick)
    }
}