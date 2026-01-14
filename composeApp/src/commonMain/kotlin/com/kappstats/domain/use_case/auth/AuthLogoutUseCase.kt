package com.kappstats.domain.use_case.auth

import com.kappstats.data.repository.auth_token.AuthTokenRepository
import com.kappstats.domain.core.FailureType
import com.kappstats.domain.core.Resource

class AuthLogoutUseCase(
    private val authTokenRepository: AuthTokenRepository
) {
    suspend operator fun invoke(): Resource<Boolean> {
        return try {
            authTokenRepository.deleteToken()
            Resource.Success(data = true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(FailureType.Unknown)
        }
    }
}