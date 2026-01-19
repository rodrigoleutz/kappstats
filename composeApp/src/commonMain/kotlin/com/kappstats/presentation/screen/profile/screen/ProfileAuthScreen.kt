package com.kappstats.presentation.screen.profile.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.kappstats.components.part.component.button.ButtonComponent
import com.kappstats.components.part.component.input.InputTextComponent
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Red20
import com.kappstats.custom_object.email.Email
import com.kappstats.presentation.constants.Tags
import com.kappstats.presentation.core.state.MainUiState
import com.kappstats.presentation.screen.profile.ProfileEvent
import com.kappstats.presentation.screen.profile.ProfileUiState
import com.kappstats.resources.Res
import com.kappstats.resources.cancel
import com.kappstats.resources.email
import com.kappstats.resources.error_email
import com.kappstats.resources.save
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Close
import compose.icons.evaicons.fill.Save
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileAuthScreen(
    mainUiState: MainUiState,
    uiState: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit,
    updateEnabled: Boolean,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
            .padding(AppDimensions.Medium.component)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(mainUiState.paddingValues.calculateTopPadding()))
        InputTextComponent(
            modifier = Modifier.fillMaxWidth().testTag(Tags.EMAIL),
            label = stringResource(Res.string.email),
            value = uiState.email,
            onChange = {
                onEvent(ProfileEvent.SetEmail(it))
            },
            errorMessage = if (Email.isValidEmail(uiState.email)) null
            else stringResource(Res.string.error_email),
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(AppDimensions.ExtraLarge.component))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonComponent(
                modifier = Modifier.weight(1f).testTag(Tags.CANCEL),
                label = stringResource(Res.string.cancel),
                icon = EvaIcons.Fill.Close,
                onClick = {
                    onCancel()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red20,
                    contentColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
            ButtonComponent(
                modifier = Modifier.weight(1f).testTag(Tags.CONFIRM),
                label = stringResource(Res.string.save),
                icon = EvaIcons.Fill.Save,
                onClick = {
                    onSave()
                },
                enabled = updateEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue20,
                    contentColor = Color.White
                )
            )
        }
        Spacer(modifier = Modifier.height(mainUiState.paddingValues.calculateBottomPadding()))
    }
}