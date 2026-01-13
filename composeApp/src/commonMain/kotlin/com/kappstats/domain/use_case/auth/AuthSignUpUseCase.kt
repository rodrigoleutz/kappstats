package com.kappstats.domain.use_case.auth

import com.kappstats.data.service.auth.AuthService
import com.kappstats.domain.core.FailureType
import com.kappstats.domain.core.Resource
import com.kappstats.dto.request.user.SignUpRequest

class AuthSignUpUseCase(
    private val authService: AuthService
) {
    suspend operator fun invoke(signUpRequest: SignUpRequest): Resource<Boolean> {
        return try {
            if(!authService.signUp(signUpRequest)) return Resource.Failure(FailureType.SaveData)
            Resource.Success(data = true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(FailureType.Unknown)
        }
    }
}