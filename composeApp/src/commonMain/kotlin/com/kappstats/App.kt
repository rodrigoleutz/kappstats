package com.kappstats

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.rememberNavBackStack
import com.kappstats.di.dataModule
import com.kappstats.di.domainModule
import com.kappstats.di.presentationModule
import com.kappstats.presentation.core.MainScreen
import com.kappstats.presentation.core.navigation.AppScreens
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatformTools

@Composable
@Preview
fun App() {
    commonStartKoin()
    MaterialTheme {
        val backStack = rememberNavBackStack(
            configuration = AppScreens.configuration,
            AppScreens.Splash
        )
        MainScreen(backStack)
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