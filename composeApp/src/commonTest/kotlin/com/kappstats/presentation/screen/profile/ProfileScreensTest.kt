package com.kappstats.presentation.screen.profile

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.kappstats.presentation.constants.Tags
import com.kappstats.presentation.screen.profile.screen.ProfileAuthScreen
import com.kappstats.util.BaseComposeIntegrationTest
import org.koin.compose.viewmodel.koinViewModel
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class ProfileScreensTest: BaseComposeIntegrationTest() {

    @Test
    fun `Test profile auth update screen`() = runComposeUiTest {
        var viewModel: ProfileViewModel? = null
        var onCancelClick: Boolean = false
        var onUpdateClick: Boolean = false
        setContent {
            viewModel = koinViewModel<ProfileViewModel>()
            val mainUiState by viewModel.stateHolder.uiState.collectAsState()
            val uiState by viewModel.uiState.collectAsState()
            ProfileAuthScreen(
                mainUiState = mainUiState,
                uiState = uiState,
                onEvent = viewModel::onEvent,
                updateEnabled = viewModel.enableUpdateAuth,
                onSave = {
                    onUpdateClick = true
                },
                onCancel = {
                    onCancelClick = true
                }
            )
        }
        val email = onNodeWithTag(Tags.EMAIL)
        email.assertExists()
        val password = onNodeWithTag(Tags.PASSWORD)
        password.assertExists()
        val updateButton = onNodeWithTag(Tags.CONFIRM)
        updateButton.assertExists()
        updateButton.assertIsNotEnabled()
        val cancelButton = onNodeWithTag(Tags.CANCEL)
        cancelButton.assertExists()
        cancelButton.performClick()
        assertEquals(true, onCancelClick)
        val passwordString = "Password#123"
        password.performTextInput(passwordString)
        assertEquals(passwordString, viewModel?.uiState?.value?.password)
        updateButton.assertIsNotEnabled()
        val emailString = "test@test.com"
        email.performTextInput(emailString)
        assertEquals(emailString, viewModel?.uiState?.value?.email)
        waitForIdle()
        updateButton.assertIsEnabled()
        updateButton.performClick()
        assertEquals(true, onUpdateClick)
    }

}