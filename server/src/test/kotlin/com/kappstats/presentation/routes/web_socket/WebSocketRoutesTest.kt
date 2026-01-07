package com.kappstats.presentation.routes.web_socket

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.custom_object.app_date_time.AppDateTimeUnit
import com.kappstats.custom_object.app_date_time.minutes
import com.kappstats.custom_object.app_date_time.seconds
import com.kappstats.data.remote.api.database.mongo.MongoApi
import com.kappstats.data.remote.container.MongoTestContainer
import com.kappstats.di.dataModule
import com.kappstats.di.domainModule
import com.kappstats.di.presentationModule
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse
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
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.koin.ktor.plugin.Koin

class WebSocketRoutesTest {

    companion object {
        val webSocketRequest = WebSocketRequest(
            action = WebSocketEvents.Status.Ping.action,
            data = Json.encodeToString(AppDateTime.now)
        )
        val json = Json.encodeToString(webSocketRequest)
    }

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
        configureWebSocket()
        configureRoutes()
    }

    private fun ApplicationTestBuilder.configuredClient() = createClient {
        this@createClient.install(ContentNegotiation) {
            json()
        }
        this@createClient.install(WebSockets)
    }

    @Test
    fun `Test webSocket connection test ping`() = testApplication {
        application {
            testModules()
        }
        val client = configuredClient()
        client.webSocket(AppEndpoints.WebSocket.route) {
            send(json)
            val frame = incoming.receive() as Frame.Text
            val jsonDecoded =
                Json.decodeFromString<WebSocketResponse>(frame.readText())
            assert(jsonDecoded.isSuccess)
            assertInstanceOf(WebSocketResponse::class.java, jsonDecoded)
            val date = Json.decodeFromString<AppDateTime>((jsonDecoded as WebSocketResponse.Success).data)
            assertInstanceOf(AppDateTime::class.java, date)
            assert(date > (AppDateTime.now-10.seconds))
        }
    }
}