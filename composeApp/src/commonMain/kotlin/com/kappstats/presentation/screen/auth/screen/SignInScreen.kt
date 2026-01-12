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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kappstats.components.part.component.button.ButtonComponent
import com.kappstats.components.part.component.input.InputTextComponent
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Green20
import com.kappstats.components.theme.Red20
import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.presentation.screen.auth.SignEvent
import com.kappstats.presentation.screen.auth.SignUiState
import com.kappstats.resources.Res
import com.kappstats.resources.cancel
import com.kappstats.resources.clear
import com.kappstats.resources.email
import com.kappstats.resources.error_email
import com.kappstats.resources.error_password
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
    onSignIn: () -> Unit,
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
            },
            errorMessage = if (uiState.email.isBlank() || Email.isValidEmail(uiState.email)) null
            else stringResource(Res.string.error_email)
        )
        Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
        InputTextComponent(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.password),
            value = uiState.password,
            onChange = {
                onEvent(SignEvent.SetPassword(it))
            },
            errorMessage = if (uiState.password.isBlank() || Password.isValidPassword(uiState.password)) null
            else stringResource(Res.string.error_password)
        )
        Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonComponent(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.clear),
                icon = EvaIcons.Fill.Close,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red20,
                    contentColor = Color.White
                ),
                onClick = {
                    onEvent(SignEvent.SetEmail(""))
                    onEvent(SignEvent.SetPassword(""))
                }
            )
            Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
            ButtonComponent(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.login),
                icon = EvaIcons.Fill.LogIn,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green20,
                    contentColor = Color.White
                ),
                enabled = Email.isValidEmail(uiState.email) && Password.isValidPassword(uiState.password),
                onClick = {
                    onSignIn()
                }
            )
        }
    }
}