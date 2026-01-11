package com.kappstats.domain.use_case.auth

import com.kappstats.data.service.auth.AuthService
import com.kappstats.domain.core.FailureType
import com.kappstats.domain.core.Resource

class AuthAuthenticateUseCase(
    private val authService: AuthService
) {
    suspend operator fun invoke(): Resource<Boolean> {
        return Resource.Failure(FailureType.Unauthorized)
    }
}