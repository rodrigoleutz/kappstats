package com.kappstats

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.kappstats.di.dataModule
import com.kappstats.di.domainModule
import com.kappstats.di.presentationModule
import com.kappstats.presentation.core.navigation.AppNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatformTools

@Composable
@Preview
fun App() {
    commonStartKoin()
    MaterialTheme {
        AppNavigation()
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