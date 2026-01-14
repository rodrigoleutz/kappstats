package com.kappstats.domain.use_case.auth

import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.data.repository.auth_token.AuthTokenRepository
import com.kappstats.data.service.auth.AuthService
import com.kappstats.domain.core.FailureType
import com.kappstats.domain.core.Resource
import com.kappstats.dto.request.user.SignInRequest
import com.kappstats.getPlatform
import com.kappstats.model.user.toPlatformData

class AuthSignInUseCase(
    private val authService: AuthService,
    private val authTokenRepository: AuthTokenRepository
) {
    suspend operator fun invoke(email: Email, password: Password): Resource<Boolean> {
        return try {
            val platformData = getPlatform().toPlatformData()
            val signInRequest = SignInRequest(
                email = email,
                password = password,
                platform = platformData
            )
            val response = authService.signIn(signInRequest)
                ?: return Resource.Failure(FailureType.Unauthorized)
            authTokenRepository.setToken(response)
            Resource.Success(data = true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(FailureType.Unknown)
        }
    }
}