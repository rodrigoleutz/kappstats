package com.kappstats

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.kappstats.di.dataModule
import com.kappstats.di.domainModule
import com.kappstats.di.presentationModule
import com.kappstats.presentation.core.MainScreen
import com.kappstats.presentation.core.navigation.AppNavigation
import com.kappstats.presentation.core.state.MainStateHolder
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatformTools

@Composable
@Preview
fun App() {
    commonStartKoin()
    MaterialTheme {
        val stateHolder = koinInject<MainStateHolder>()
        val uiState by stateHolder.uiState.collectAsState()
        MainScreen(
            uiState = uiState,
            onEvent = stateHolder::onMainEvent,
            content = {
                AppNavigation(
                    stateHolder
                )
            }
        )
    }
}

val commonModules = listOf(
    dataModule,
    domainModule,
    presentationModule
)

fun commonStartKoin() {
    if (KoinPlatformTools.defaultContext().getOrNull() == null) {
        startKoin {
            modules(commonModules)
        }
    }
}