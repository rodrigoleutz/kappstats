package com.kappstats.data.service.auth

import com.kappstats.constants.USERNAME
import com.kappstats.custom_object.username.Username
import com.kappstats.data.remote.data_source.RemoteDataSource
import com.kappstats.dto.request.user.SignInRequest
import com.kappstats.dto.request.user.SignUpRequest
import com.kappstats.endpoint.AppEndpoints
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class AuthServiceImpl(
    private val remoteDataSource: RemoteDataSource
) : AuthService {
    override suspend fun authenticate(token: String): Boolean {
        val response = remoteDataSource.client.get(AppEndpoints.Api.User.Authenticate.route) {
            createRequest(token)
        }
        return response.status == HttpStatusCode.OK
    }

    override suspend fun hasUsername(username: Username): Boolean {
        val response = remoteDataSource.client.get(AppEndpoints.Api.User.HasUsername.route) {
            createRequest()
            parameter(USERNAME, username.asString)
        }
        return response.status == HttpStatusCode.OK
    }

    override suspend fun signIn(signInRequest: SignInRequest): String? {
        val response = remoteDataSource.client.post(AppEndpoints.Api.User.SignIn.route) {
            createRequest()
        }
        return response.body()
    }

    override suspend fun signUp(signUpRequest: SignUpRequest): Boolean {
        val response = remoteDataSource.client.post(AppEndpoints.Api.User.SignUp.route) {
            createRequest()
            setBody(signUpRequest)
        }
        return response.status == HttpStatusCode.Created
    }

    private fun HttpRequestBuilder.createRequest(token: String? = null) {
        contentType(ContentType.Application.Json)
        if (!token.isNullOrBlank()) bearerAuth(token)
    }
}