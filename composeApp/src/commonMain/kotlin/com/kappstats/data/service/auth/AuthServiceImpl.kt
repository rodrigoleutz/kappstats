package com.kappstats.data.service.auth

import com.kappstats.dto.request.user.SignInRequest
import com.kappstats.dto.request.user.SignUpRequest

class AuthServiceImpl: AuthService {
    override suspend fun authenticate(token: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(signInRequest: SignInRequest): String? {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(signUpRequest: SignUpRequest): Boolean {
        TODO("Not yet implemented")
    }
}