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
import com.kappstats.presentation.constants.Tags
import com.kappstats.presentation.screen.auth.screen.SignInScreen
import com.kappstats.presentation.screen.auth.screen.SignUpScreen
import com.kappstats.util.BaseComposeIntegrationTest
import org.koin.compose.viewmodel.koinViewModel
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class SignScreensTest: BaseComposeIntegrationTest() {

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
        val email = onNodeWithTag(Tags.EMAIL)
        email.assertExists()
        val password = onNodeWithTag(Tags.PASSWORD)
        password.assertExists()
        val clear = onNodeWithTag(Tags.CLEAR)
        clear.assertExists()
        val login = onNodeWithTag(Tags.CONFIRM)
        login.assertExists()
        login.assertIsNotEnabled()
        with(email) {
            performClick()
            performTextReplacement("test@test.com")
        }
        waitForIdle()
        assertEquals("test@test.com", viewModel?.uiState?.value?.email)
        with(password) {
            performClick()
            performTextReplacement("Password#123")
        }
        waitForIdle()
        assertEquals("Password#123", viewModel?.uiState?.value?.password)
        login.performClick()
        assertEquals(true, signInClicked)
        assertEquals("test@test.com", viewModel?.uiState?.value?.email)
        assertEquals("Password#123", viewModel?.uiState?.value?.password)
        clear.performClick()
        assertEquals("", viewModel?.uiState?.value?.email)
        assertEquals("", viewModel?.uiState?.value?.password)
        login.assertIsNotEnabled()
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

        val nameNode = onNodeWithTag(Tags.NAME)
        nameNode.assertExists()
        val emailNode = onNodeWithTag(Tags.EMAIL)
        emailNode.assertExists()
        val usernameNode = onNodeWithTag(Tags.USERNAME)
        usernameNode.assertExists()
        val passwordNode = onNodeWithTag(Tags.PASSWORD)
        passwordNode.assertExists()
        val passwordConfirmNode = onNodeWithTag(Tags.PASSWORD_CONFIRM)
        passwordConfirmNode.assertExists()
        val registerButtonNode = onNodeWithTag(Tags.CONFIRM)
        registerButtonNode.assertExists()
        val clearButtonNode = onNodeWithTag(Tags.CLEAR)
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
        assertEquals("Test Name", viewModel?.uiState?.value?.name)
        assertEquals("test@test.com", viewModel?.uiState?.value?.email)
        assertEquals("test123", viewModel?.uiState?.value?.username)
        assertEquals("Password#123", viewModel?.uiState?.value?.password)
        clearButtonNode.performClick()
        assertEquals("", viewModel?.uiState?.value?.name)
        assertEquals("", viewModel?.uiState?.value?.email)
        assertEquals("", viewModel?.uiState?.value?.username)
        assertEquals("", viewModel?.uiState?.value?.password)
        registerButtonNode.assertIsNotEnabled()
    }
}