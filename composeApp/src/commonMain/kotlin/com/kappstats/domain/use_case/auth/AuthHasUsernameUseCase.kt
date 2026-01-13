package com.kappstats.domain.use_case.auth

import com.kappstats.custom_object.username.Username
import com.kappstats.data.service.auth.AuthService
import com.kappstats.domain.core.FailureType
import com.kappstats.domain.core.Resource


class AuthHasUsernameUseCase(
    private val authService: AuthService
) {
    suspend operator fun invoke(username: Username): Resource<Boolean> {
        return try {
            val response = authService.hasUsername(username)
            Resource.Success(data = response)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(FailureType.Unknown)
        }
    }
}