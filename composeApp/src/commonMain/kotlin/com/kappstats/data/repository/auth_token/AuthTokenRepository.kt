package com.kappstats.data.repository.auth_token

interface AuthTokenRepository {
    suspend fun getToken(): String?
    suspend fun setToken(token: String)
}