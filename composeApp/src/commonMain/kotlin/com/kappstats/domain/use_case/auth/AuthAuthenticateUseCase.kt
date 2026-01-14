package com.kappstats.domain.use_case.auth

import com.kappstats.data.repository.auth_token.AuthTokenRepository
import com.kappstats.data.service.auth.AuthService
import com.kappstats.domain.core.FailureType
import com.kappstats.domain.core.Resource

class AuthAuthenticateUseCase(
    private val authService: AuthService,
    private val authTokenRepository: AuthTokenRepository
) {
    suspend operator fun invoke(): Resource<Boolean> {
        return try {
            val token = authTokenRepository.getToken()
                ?: return Resource.Failure(FailureType.LoadData)
            val response = authService.authenticate(token)
            if(!response) return Resource.Failure(FailureType.Unauthorized)
            Resource.Success(data = true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(FailureType.Network)
        }
    }
}