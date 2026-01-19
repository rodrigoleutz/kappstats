package com.kappstats.util

import androidx.compose.ui.test.ExperimentalTestApi
import com.kappstats.di.dataModule
import com.kappstats.di.domainModule
import com.kappstats.di.presentationModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalTestApi::class)
abstract class BaseComposeIntegrationTest {

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(dataModule, domainModule, presentationModule)
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

}