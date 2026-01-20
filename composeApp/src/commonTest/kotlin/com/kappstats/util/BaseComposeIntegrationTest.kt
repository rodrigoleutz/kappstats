@file:OptIn(ExperimentalTestApi::class)

package com.kappstats.util

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.kappstats.di.dataModule
import com.kappstats.di.domainModule
import com.kappstats.di.presentationModule
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest


abstract class BaseComposeIntegrationTest: KoinComponent {


    @BeforeTest
    open fun setUp() {
        startKoinModules()
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    fun startKoinModules(
        data: Module = dataModule,
        domain: Module = domainModule,
        presentation: Module = presentationModule
    ) {
        startKoin {
            modules(data, domain, presentation)
        }
    }

    fun baseComposeUiTest(block: ComposeUiTest.() -> Unit) = runComposeUiTest {
        block()
    }

}