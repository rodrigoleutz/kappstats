package com.kappstats.presentation.screen.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.kappstats.presentation.core.navigation.AppScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    backStack: NavBackStack<NavKey>
) {
    LaunchedEffect(Unit) {
        delay(3_000)
        backStack.add(AppScreens.Home)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("splash")
    }
}