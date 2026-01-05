package com.kappstats.presentation.routes.web_socket

import com.kappstats.data.remote.api.database.mongo.MongoApi
import com.kappstats.data.remote.container.MongoTestContainer
import com.kappstats.di.dataModule
import com.kappstats.di.domainModule
import com.kappstats.di.presentationModule
import com.kappstats.endpoint.AppEndpoints
import com.kappstats.plugin.configureLogger
import com.kappstats.plugin.configureRoutes
import com.kappstats.plugin.configureSecurity
import com.kappstats.plugin.configureSerialization
import com.kappstats.plugin.configureWebSocket
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.koin.ktor.plugin.Koin

class WebSocketRoutesTest {



    private fun Application.koinTest() {
        val databaseTestModule = org.koin.dsl.module {
            single {
                MongoApi(
                    MongoTestContainer.connectionString,
                    "KAppStatsTest"
                )
            }
        }
        install(Koin) {
            modules(
                databaseTestModule,
                dataModule,
                domainModule,
                presentationModule
            )
        }
    }

    private fun Application.testModules() {
        configureLogger()
        configureSerialization()
        koinTest()
        configureSecurity()
        configureRoutes()
        configureWebSocket()
    }

    private fun ApplicationTestBuilder.configuredClient() = createClient {
        this@createClient.install(ContentNegotiation) {
            json()
        }
        this@createClient.install(WebSockets)
    }

//    @Test
//    fun `Test webSocket connection`() = testApplication {
//        application {
//            testModules()
//        }
//        val client = configuredClient()
//        client.webSocket(AppEndpoints.WebSocket.route) {
//
//        }
//    }
}