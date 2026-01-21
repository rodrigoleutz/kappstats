package com.kappstats.presentation.routes.api.app_monitor

import com.kappstats.endpoint.AppEndpoints
import com.kappstats.test_util.BaseIntegrationTest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.header
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class AppMonitorRoutesTest : BaseIntegrationTest() {


    @Test
    fun `Test app monitor test route`() = baseTestApplication { callProvider, client ->
        val response = client.get(AppEndpoints.Api.AppMonitor.Test.fullPath) {
            header("X-AppMonitor", "Test A")
        }
        val call = callProvider()
        val appMonitorHeader = call?.request?.header("X-AppMonitor")
        assertEquals("Test A", appMonitorHeader)
        assertNotEquals("Test B", appMonitorHeader)
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Test AppMonitor route OK", response.bodyAsText())
    }

}