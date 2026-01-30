package com.kappstats.test_util

import com.kappstats.data.data_source.remote.api.database.mongo.MongoApi
import com.kappstats.data.data_source.remote.api.email.EmailApiImpl
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
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.cancel
import org.junit.jupiter.api.AfterEach
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

abstract class BaseIntegrationTest {

    @AfterEach
    fun tearDown() {

        stopKoin()
    }

    /**
     * Server Integration Tests Configs
     * @author Rodrigo Leutz
     */

    val mongoApi = MongoApi(
        MongoTestContainer.connectionString,
        "KAppStatsTest"
    )
    val emailApi = EmailApiImpl()

    /**
     * Koin for tests
     * @author Rodrigo Leutz
     */
    private fun Application.koinTest() {
        val databaseTestModule = org.koin.dsl.module {
            single { mongoApi }
        }
        val emailApiTestModule = module {
            single { emailApi }
        }
        install(Koin) {
            modules(
                databaseTestModule,
                emailApiTestModule,
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
        block: suspend ApplicationTestBuilder.(callProvider: () -> ApplicationCall?, client: HttpClient) -> Unit
    ) = testApplication {
        var applicationCall: ApplicationCall? = null
        application {
            modules()
            intercept(ApplicationCallPipeline.Plugins) {
                applicationCall = call
                proceed()
            }
        }
        val testClient = configuredClient()
        try {
            block({applicationCall}, testClient)
        } finally {
            testClient.close()
        }
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
        this@createClient.install(DefaultRequest) {
            header("X-Forwarded-For", "123.123.123.123")
        }
    }
}