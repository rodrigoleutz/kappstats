package com.kappstats.presentation.routes.api.user

import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.custom_object.username.Username
import com.kappstats.data.remote.api.database.mongo.MongoApi
import com.kappstats.data.remote.container.MongoTestContainer
import com.kappstats.di.dataModule
import com.kappstats.di.domainModule
import com.kappstats.di.presentationModule
import com.kappstats.dto.request.user.SignInRequest
import com.kappstats.dto.request.user.SignUpRequest
import com.kappstats.endpoint.AppEndpoints
import com.kappstats.plugin.configureLogger
import com.kappstats.plugin.configureRoutes
import com.kappstats.plugin.configureSecurity
import com.kappstats.plugin.configureSerialization
import com.kappstats.plugin.configureWebSocket
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.koin.ktor.plugin.Koin

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserRoutesTest {

    companion object {
        private val signUp = SignUpRequest(
            email = Email("test@test.com"),
            username = Username("test123"),
            name = "Test User",
            password = Password("Password#123")
        )
        private val signIn = SignInRequest(
            email = signUp.email,
            password = signUp.password
        )
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
        configureRoutes()
        configureWebSocket()
    }

    @BeforeEach
    fun setUp() {

    }

    @AfterEach
    fun setDown() = runTest {

    }

    fun ApplicationTestBuilder.configuredClient() = createClient {
        this@createClient.install(ContentNegotiation) {
            json()
        }
    }

    @Test
    @Order(1)
    fun `SignUp route test`() = testApplication {
        application {
            testModules()
        }
        val client = configuredClient()
        val signUpRequest = client.post(AppEndpoints.Api.User.SignUp.route) {
            contentType(ContentType.Application.Json)
            setBody(signUp)
        }
        assertEquals(HttpStatusCode.Created, signUpRequest.status)
    }

    @Test
    @Order(2)
    fun `SignIn and authenticate route test`() = testApplication {
        application {
            testModules()
        }
        val client = configuredClient()
        val signInRequest = client.post(AppEndpoints.Api.User.SignIn.route) {
            contentType(ContentType.Application.Json)
            setBody(signIn)
        }
        assertEquals(HttpStatusCode.OK, signInRequest.status)
        val token = signInRequest.bodyAsText()
        assert(token.isNotBlank())
        val authenticateRequest = client.get(AppEndpoints.Api.User.Authenticate.route) {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.OK, authenticateRequest.status)
    }

}