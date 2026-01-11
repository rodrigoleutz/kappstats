package com.kappstats.domain.use_case.auth

import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.domain.core.FailureType
import com.kappstats.domain.core.Resource

class AuthSignInUseCase(

) {
    suspend operator fun invoke(email: Email, password: Password): Resource<Boolean> {
        return try {
            Resource.Success(data = true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(FailureType.Unknown)
        }
    }
}