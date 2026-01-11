package com.kappstats.presentation.screen.auth.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kappstats.components.part.component.button.ButtonComponent
import com.kappstats.components.part.component.input.InputTextComponent
import com.kappstats.components.theme.AppDimensions
import com.kappstats.presentation.screen.auth.SignEvent
import com.kappstats.presentation.screen.auth.SignUiState
import com.kappstats.resources.Res
import com.kappstats.resources.cancel
import com.kappstats.resources.email
import com.kappstats.resources.login
import com.kappstats.resources.password
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Close
import compose.icons.evaicons.fill.LogIn
import org.jetbrains.compose.resources.stringResource

@Composable
fun SignInScreen(
    uiState: SignUiState,
    onEvent: (SignEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(AppDimensions.Medium.component),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InputTextComponent(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.email),
            value = uiState.email,
            onChange = {
                onEvent(SignEvent.SetEmail(it))
            }
        )
        Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
        InputTextComponent(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.password),
            value = uiState.password,
            onChange = {
                onEvent(SignEvent.SetPassword(it))
            }
        )
        Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonComponent(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.cancel),
                icon = EvaIcons.Fill.Close,
                onClick = {

                }
            )
            Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
            ButtonComponent(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.login),
                icon = EvaIcons.Fill.LogIn,
                onClick = {
                    onEvent(SignEvent.SignIn)
                }
            )
        }
    }
}