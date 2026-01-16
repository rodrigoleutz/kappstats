package com.kappstats.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.kappstats.domain.data_state.user.UserState
import com.kappstats.domain.web_socket.actions.user.AuthUserGetUserInfoAction
import com.kappstats.presentation.core.state.MainEvent
import com.kappstats.resources.Res
import com.kappstats.resources.welcome
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    userState: UserState,
    onMainEvent: (MainEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onMainEvent(MainEvent.SetHasTopBar(true))
    }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        userState.myProfile?.let { profile ->
            Text(
                text = stringResource(Res.string.welcome),
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = profile.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = profile.username.asString,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Thin
            )
        }
    }
}