package com.kappstats.data.service.auth

import com.kappstats.dto.request.user.SignInRequest
import com.kappstats.dto.request.user.SignUpRequest

interface AuthService {
    suspend fun authenticate(token: String): Boolean
    suspend fun signIn(signInRequest: SignInRequest): String?
    suspend fun signUp(signUpRequest: SignUpRequest): Boolean
}