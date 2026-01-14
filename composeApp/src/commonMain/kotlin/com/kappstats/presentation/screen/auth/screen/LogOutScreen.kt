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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import com.kappstats.components.part.component.button.ButtonComponent
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Red20
import com.kappstats.resources.Res
import com.kappstats.resources.cancel
import com.kappstats.resources.logout
import com.kappstats.resources.logout_message
import org.jetbrains.compose.resources.stringResource

@Composable
fun LogOutScreen(
    modifier: Modifier = Modifier,
    cancel: () -> Unit,
    logOut: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize().padding(AppDimensions.Medium.component),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.logout_message),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(AppDimensions.ExtraExtraLarge.component))
        Row(modifier = Modifier.fillMaxWidth()) {
            ButtonComponent(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.cancel),
                onClick = {
                    cancel()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red20,
                    contentColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
            ButtonComponent(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.logout),
                onClick = {
                    logOut()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue20,
                    contentColor = Color.White
                )
            )
        }
    }
}