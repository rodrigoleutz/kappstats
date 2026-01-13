package com.kappstats.domain.use_case.user

import com.kappstats.custom_object.username.Username
import com.kappstats.data.repository.user.ProfileRepository
import com.kappstats.domain.core.resource.Resource
import com.kappstats.model.user.Profile
import io.ktor.http.HttpStatusCode

class UserHasUsernameUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(username: Username): Resource<Boolean> {
        return try {
            val profiles = profileRepository.generic.getListByProperty(Profile::username, username)
            if(profiles.isEmpty()) return Resource.Failure(HttpStatusCode.NotFound)
            Resource.Success(data = true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(HttpStatusCode.ExpectationFailed)
        }
    }
}