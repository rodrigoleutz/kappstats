package com.kappstats.presentation.routes.api.user

import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.custom_object.username.Username
import com.kappstats.dto.request.user.SignInRequest
import com.kappstats.dto.request.user.SignUpRequest
import com.kappstats.endpoint.AppEndpoints
import com.kappstats.test_util.BaseIntegrationTest
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserRoutesTest: BaseIntegrationTest() {

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

    @Test
    @Order(1)
    fun `SignUp route test`() = baseTestApplication { client ->
        val signUpRequest = client.post(AppEndpoints.Api.User.SignUp.route) {
            contentType(ContentType.Application.Json)
            setBody(signUp)
        }
        assertEquals(HttpStatusCode.Created, signUpRequest.status)
    }

    @Test
    @Order(2)
    fun `SignIn and authenticate route test`() = baseTestApplication { client ->
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