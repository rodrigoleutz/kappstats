package com.kappstats.domain.use_case.user

import com.kappstats.data.repository.user.AuthRepository
import com.kappstats.data.repository.user.ProfileRepository
import com.kappstats.domain.core.resource.Resource
import com.kappstats.domain.core.security.hashing.HashingService
import com.kappstats.dto.request.user.SignUpRequest
import com.kappstats.model.user.Auth
import com.kappstats.model.user.Profile
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.bson.types.ObjectId

class UserSignUpUseCase(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val hashingService: HashingService
) {

    suspend operator fun invoke(signUpRequest: SignUpRequest): Resource<Boolean> {
        val saltedHash = hashingService.generateSaltedHash(signUpRequest.password.asString)
        val profileId = ObjectId().toHexString()
        val authId = ObjectId().toHexString()
        return try {
            val authAddTask = CoroutineScope(Dispatchers.IO).async {
                val auth = Auth(
                    id = authId,
                    profileId = profileId,
                    email = signUpRequest.email,
                )
                authRepository.add(auth, saltedHash)
            }
            val profileAddTask = CoroutineScope(Dispatchers.IO).async {
                val profile = Profile(
                    id = profileId,
                    name = signUpRequest.name,
                    username = signUpRequest.username,
                    bio = ""
                )
                profileRepository.generic.add(profile)
            }
            awaitAll(authAddTask, profileAddTask)
            Resource.Success(data = true, status = HttpStatusCode.Created)
        } catch (e: Exception) {
            profileRepository.generic.deleteById(profileId)
            authRepository.deleteById(authId)
            Resource.Failure(
                status = HttpStatusCode.ExpectationFailed,
                message = e.message,
                origin = this@UserSignUpUseCase
            )
        }
    }
}