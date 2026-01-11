package com.kappstats.test_util

import com.kappstats.data.remote.api.database.mongo.MongoApi
import com.kappstats.di.dataModule
import com.kappstats.di.domainModule
import com.kappstats.di.presentationModule
import com.kappstats.plugin.configureLogger
import com.kappstats.plugin.configureRoutes
import com.kappstats.plugin.configureSecurity
import com.kappstats.plugin.configureSerialization
import com.kappstats.plugin.configureWebSocket
import com.kappstats.test_util.container.MongoTestContainer
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import org.koin.ktor.plugin.Koin

abstract class BaseIntegrationTest {


    /**
     * Server Integration Tests Configs
     * @author Rodrigo Leutz
     */

    val mongoApi = MongoApi(
        MongoTestContainer.connectionString,
        "KAppStatsTest"
    )

    /**
     * Koin for tests
     * @author Rodrigo Leutz
     */
    private fun Application.koinTest() {
        val databaseTestModule = org.koin.dsl.module {
            single { mongoApi }
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

    /**
     * testServerModules
     * @param logger replace logger config
     * @param serialization replace serialization config
     * @param koin replace koin config
     * @param security replace security config
     * @param webSocket replace webSocket config
     * @param routes replace routes config
     * @author Rodrigo Leutz
     */
    fun Application.testServerModules(
        logger: Application.() -> Unit = { configureLogger() },
        serialization: Application.() -> Unit = { configureSerialization() },
        koin: Application.() -> Unit = { koinTest() },
        security: Application.() -> Unit = { configureSecurity() },
        webSocket: Application.() -> Unit = { configureWebSocket() },
        routes: Application.() -> Unit = { configureRoutes() }
    ) {
        logger()
        serialization()
        koin()
        security()
        webSocket()
        routes()
    }

    fun baseTestApplication(
        modules: Application.() -> Unit = { testServerModules() },
        block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit
    ) = testApplication {
        application {
            modules()
        }
        val testClient = configuredClient()
        block(testClient)
    }

    /**
     * Client Integration Tests Config
     * @author Rodrigo Leutz
     */

    private fun ApplicationTestBuilder.configuredClient() = createClient {
        this@createClient.install(ContentNegotiation) {
            json()
        }
        this@createClient.install(WebSockets)
    }
}