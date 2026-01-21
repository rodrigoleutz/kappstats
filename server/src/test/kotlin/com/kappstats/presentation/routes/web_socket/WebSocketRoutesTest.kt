package com.kappstats.presentation.routes.web_socket

import com.kappstats.Platform
import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.custom_object.app_date_time.seconds
import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.custom_object.username.Username
import com.kappstats.dto.request.user.SignInRequest
import com.kappstats.dto.request.user.SignUpRequest
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse
import com.kappstats.endpoint.AppEndpoints
import com.kappstats.model.user.Auth
import com.kappstats.model.user.AuthToken
import com.kappstats.model.user.PlatformData
import com.kappstats.model.user.Profile
import com.kappstats.test_util.BaseIntegrationTest
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import kotlin.collections.emptyList
import kotlin.test.assertNotEquals

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class WebSocketRoutesTest : BaseIntegrationTest() {

    companion object {

        private val platform = PlatformData(
            name = Platform.PlatformType.Desktop,
            userAgent = "Test agent 123"
        )
        private val signUp = SignUpRequest(
            email = Email("test123@test.com"),
            username = Username("test321"),
            name = "Test User",
            password = Password("Password#123")
        )
        private val signIn = SignInRequest(
            email = signUp.email,
            password = signUp.password,
            platform = platform
        )

        val webSocketRequestPing = WebSocketRequest(
            action = WebSocketEvents.Status.Ping.action,
            data = Json.encodeToString(AppDateTime.now)
        )
        val jsonPing = Json.encodeToString(webSocketRequestPing)

        val webSocketRequestUserInfo = WebSocketRequest(
            action = WebSocketEvents.Authenticate.User.GetMyUserInfo.action
        )
        val jsonUserInfo = Json.encodeToString(webSocketRequestUserInfo)

        lateinit var token: String
    }


    @Test
    @Order(1)
    fun `Test webSocket connection test ping`() = baseTestApplication { _, client ->
        client.webSocket(AppEndpoints.WebSocket.fullPath) {
            send(jsonPing)
            val frame = incoming.receive() as Frame.Text
            val jsonDecoded =
                Json.decodeFromString<WebSocketResponse>(frame.readText())
            assert(jsonDecoded.isSuccess)
            assertInstanceOf(WebSocketResponse::class.java, jsonDecoded)
            val date =
                Json.decodeFromString<AppDateTime>((jsonDecoded as WebSocketResponse.Success).data)
            assertInstanceOf(AppDateTime::class.java, date)
            assert(date > (AppDateTime.now - 10.seconds))
        }
    }

    @Test
    @Order(2)
    fun `SignUp route test`() = baseTestApplication { _, client ->
        val signUpRequest = client.post(AppEndpoints.Api.User.SignUp.fullPath) {
            contentType(ContentType.Application.Json)
            setBody(signUp)
        }
        assertEquals(HttpStatusCode.Created, signUpRequest.status)
    }

    @Test
    @Order(3)
    fun `SignIn and authenticate route test`() = baseTestApplication { _, client ->
        val signInRequest = client.post(AppEndpoints.Api.User.SignIn.fullPath) {
            contentType(ContentType.Application.Json)
            setBody(signIn)
        }
        assertEquals(HttpStatusCode.OK, signInRequest.status)
        token = signInRequest.bodyAsText()
        assert(token.isNotBlank())
        val authenticateRequest = client.get(AppEndpoints.Api.User.Authenticate.fullPath) {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.OK, authenticateRequest.status)
    }

    @Test
    @Order(4)
    fun `Test webSocket auth connection test get user info`() = baseTestApplication { _, client ->
        client.webSocket(
            AppEndpoints.WebSocket.Auth.fullPath,
            request = {
                bearerAuth(token)
            }
        ) {
            send(jsonUserInfo)
            val frame = incoming.receive() as Frame.Text
            val jsonDecoded =
                Json.decodeFromString<WebSocketResponse>(frame.readText())
            assert(jsonDecoded.isSuccess)
            assertInstanceOf(WebSocketResponse::class.java, jsonDecoded)
            val tripleUserInfo =
                Json.decodeFromString<Triple<Auth, AuthToken, Profile>>(
                    (jsonDecoded as WebSocketResponse.Success).data
                )
            assertEquals(signUp.email, tripleUserInfo.first.email)
            assertEquals(signUp.name, tripleUserInfo.third.name)
            assertEquals(signUp.username, tripleUserInfo.third.username)
        }
    }

    @Test
    @Order(4)
    fun `Test webSocket update profile`() = baseTestApplication { _, client ->
        client.webSocket(
            AppEndpoints.WebSocket.Auth.fullPath,
            request = {
                bearerAuth(token)
            }
        ) {
            send(jsonUserInfo)
            val frame = incoming.receive() as Frame.Text
            val jsonDecoded =
                Json.decodeFromString<WebSocketResponse>(frame.readText())
            assert(jsonDecoded.isSuccess)
            assertInstanceOf(WebSocketResponse::class.java, jsonDecoded)
            val tripleUserInfo =
                Json.decodeFromString<Triple<Auth, AuthToken, Profile>>(
                    (jsonDecoded as WebSocketResponse.Success).data
                )
            assertEquals(signUp.email, tripleUserInfo.first.email)
            assertEquals(signUp.name, tripleUserInfo.third.name)
            assertEquals(signUp.username, tripleUserInfo.third.username)
            val profile = tripleUserInfo.third.copy(
                name = "Test name info",
                username = Username("testName2")
            )
            val webSocketRequest = WebSocketRequest(
                data = Json.encodeToString(profile),
                action = WebSocketEvents.Authenticate.User.ProfileUpdate.action
            )
            send(Json.encodeToString(webSocketRequest))
            val frame1 = incoming.receive() as Frame.Text
            val webSocketResponseProfile =
                Json.decodeFromString<WebSocketResponse>(frame1.readText())
            assert(webSocketResponseProfile.isSuccess)
            val profileDecoded = Json.decodeFromString<Profile>(
                (webSocketResponseProfile as WebSocketResponse.Success).data
            )
            assertEquals(profile.name, profileDecoded.name)
            assertEquals(profile.username, profileDecoded.username)
            assertNotEquals(profile.updatedAt, profileDecoded.updatedAt)
            assertEquals(profile.updatedAt.size + 1, profileDecoded.updatedAt.size)
            assertEquals(emptyList<String>(), webSocketResponseProfile.profiles)
            assertEquals(null, webSocketResponseProfile.webSocketId)
        }
    }

    @Test
    @Order(5)
    fun `Test webSocket update auth`() = baseTestApplication { _, client ->
        client.webSocket(
            AppEndpoints.WebSocket.Auth.fullPath,
            request = {
                bearerAuth(token)
            }
        ) {
            send(jsonUserInfo)
            val frame = incoming.receive() as Frame.Text
            val jsonDecoded =
                Json.decodeFromString<WebSocketResponse>(frame.readText())
            assert(jsonDecoded.isSuccess)
            assertInstanceOf(WebSocketResponse::class.java, jsonDecoded)
            val tripleUserInfo =
                Json.decodeFromString<Triple<Auth, AuthToken, Profile>>(
                    (jsonDecoded as WebSocketResponse.Success).data
                )
            assertEquals(signUp.email, tripleUserInfo.first.email)
            val auth = tripleUserInfo.first.copy(
                email = Email("hello@test.com"),
            )
            val webSocketRequest = WebSocketRequest(
                data = Json.encodeToString(Pair(Pair(auth.email, null), signUp.password)),
                action = WebSocketEvents.Authenticate.User.AuthUpdate.action
            )
            send(Json.encodeToString(webSocketRequest))
            val frame1 = incoming.receive() as Frame.Text
            val webSocketResponseAuth =
                Json.decodeFromString<WebSocketResponse>(frame1.readText())
            assert(webSocketResponseAuth.isSuccess)
            val authDecoded = Json.decodeFromString<Auth>(
                (webSocketResponseAuth as WebSocketResponse.Success).data
            )
            assertEquals(auth.email, authDecoded.email)
            assertEquals(auth.createdAt, authDecoded.createdAt)
            assertNotEquals(auth.updatedAt, authDecoded.updatedAt)
            assertEquals(auth.updatedAt.size + 1, authDecoded.updatedAt.size)
            assertEquals(emptyList<String>(), webSocketResponseAuth.profiles)
            assertEquals(null, webSocketResponseAuth.webSocketId)
            val webSocketRequestChangePassword = WebSocketRequest(
                data = Json.encodeToString(
                    Pair(
                        Pair(auth.email, Password("Hello!123")),
                        signUp.password
                    )
                ),
                action = WebSocketEvents.Authenticate.User.AuthUpdate.action
            )
            val jsonChangePassword = Json.encodeToString(webSocketRequestChangePassword)
            send(jsonChangePassword)
            val webSocketResponseChangePasswordFrame = incoming.receive() as Frame.Text
            val webSocketResponseChangePasswordDecoded =
                Json.decodeFromString<WebSocketResponse>(webSocketResponseChangePasswordFrame.readText())
            val webSocketResponseChangePasswordAuth = Json.decodeFromString<Auth>(
                (webSocketResponseChangePasswordDecoded as WebSocketResponse.Success).data
            )
            assert(webSocketResponseChangePasswordDecoded.isSuccess)
            assertEquals(auth.email, webSocketResponseChangePasswordAuth.email)
            send(jsonChangePassword)
            val changePasswordErrorFrame = incoming.receive() as Frame.Text
            val changePasswordErrorDecoded =
                Json.decodeFromString<WebSocketResponse>(changePasswordErrorFrame.readText())
            assert(!changePasswordErrorDecoded.isSuccess)
            assertEquals(
                WebSocketResponse.Companion.FailureType.Unauthorized,
                (changePasswordErrorDecoded as WebSocketResponse.Failure).failureType
            )
        }
    }
}