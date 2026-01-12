package com.kappstats.presentation.routes.api.user

import com.kappstats.Platform
import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.custom_object.username.Username
import com.kappstats.data.entity.user.AuthEntity
import com.kappstats.data.entity.user.AuthTokenEntity
import com.kappstats.dto.request.user.SignInRequest
import com.kappstats.dto.request.user.SignUpRequest
import com.kappstats.endpoint.AppEndpoints
import com.kappstats.model.user.PlatformData
import com.kappstats.test_util.BaseIntegrationTest
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import kotlin.test.assertNotEquals

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserRoutesTest : BaseIntegrationTest() {

    companion object {

        private val platform = PlatformData(
            name = Platform.PlatformType.Desktop,
            userAgent = "Test agent"
        )
        private val signUp = SignUpRequest(
            email = Email("test@test.com"),
            username = Username("test123"),
            name = "Test User",
            password = Password("Password#123")
        )
        private val signIn = SignInRequest(
            email = signUp.email,
            password = signUp.password,
            platform = platform
        )

        lateinit var token: String
    }

    @Test
    @Order(1)
    fun `SignUp route test`() = baseTestApplication { client ->
        val signUpRequest = client.post(AppEndpoints.Api.User.SignUp.fullPath) {
            contentType(ContentType.Application.Json)
            setBody(signUp)
        }
        assertEquals(HttpStatusCode.Created, signUpRequest.status)
    }

    @Test
    @Order(2)
    fun `SignIn and authenticate route test`() = baseTestApplication { client ->
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
    @Order(3)
    fun `Authenticate route test fail deactivated token`() = baseTestApplication { client ->
        val authCollection =
            mongoApi.database.getCollection<AuthEntity>(AuthEntity::class.simpleName.toString())
        val authList = authCollection.find().toList()
        assertEquals(1, authList.size)
        val authTokenCollection =
            mongoApi.database.getCollection<AuthTokenEntity>(AuthTokenEntity::class.simpleName.toString())
        val authTokenList = authTokenCollection.find(
            Filters.eq(
                AuthTokenEntity::authId.name,
                authList[0]._id.toHexString()
            )
        ).toList()
        assertEquals(1, authTokenList.size)
        val tokens = authTokenList[0].tokens
        assertEquals(1, tokens.size)
        assertEquals(tokens[0].platform, platform)
        val updatedToken = tokens[0].copy(isActive = false)
        assertNotEquals(tokens[0], updatedToken)
        val update = authTokenCollection.updateOne(
            Filters.eq(AuthTokenEntity::authId.name, authList[0]._id.toHexString()),
            Updates.set(AuthTokenEntity::tokens.name, listOf(updatedToken))
        )
        assertEquals(1, update.modifiedCount)
        val authenticateRequest = client.get(AppEndpoints.Api.User.Authenticate.fullPath) {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.Unauthorized, authenticateRequest.status)
    }

}