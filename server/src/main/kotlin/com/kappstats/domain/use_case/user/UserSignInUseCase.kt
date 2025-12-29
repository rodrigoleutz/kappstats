package com.kappstats.domain.use_case.user

import com.kappstats.data.repository.user.AuthRepository
import com.kappstats.domain.constants.DomainConstants
import com.kappstats.domain.core.resource.Resource
import com.kappstats.domain.core.security.hashing.HashingService
import com.kappstats.domain.core.security.token.TokenClaim
import com.kappstats.domain.core.security.token.TokenService
import com.kappstats.dto.request.user.SignInRequest
import io.ktor.http.HttpStatusCode
import java.util.UUID

class UserSignInUseCase(
    private val authRepository: AuthRepository,
    private val hashingService: HashingService,
    private val tokenService: TokenService
) {
    suspend operator fun invoke(signInRequest: SignInRequest): Resource<String> {
        return try {
            val authAndSaltedHash = authRepository.getAuthAndSaltedHash(signInRequest.email)
                ?: return Resource.Failure(
                    status = HttpStatusCode.Unauthorized,
                    message = "User not exists.",
                    origin = this@UserSignInUseCase
                )
            if (!hashingService.verifyHash(signInRequest.password.asString, authAndSaltedHash.second))
                return Resource.Failure(
                    status = HttpStatusCode.Unauthorized,
                    message = "Unauthorized verify salted hash.",
                    origin = this@UserSignInUseCase
                )
            val token = tokenService.generate(
                TokenClaim(
                    DomainConstants.AUTH_ID, authAndSaltedHash.first.id
                ),
                TokenClaim(
                    DomainConstants.PROFILE_ID, authAndSaltedHash.first.profileId
                ),
                TokenClaim(
                    DomainConstants.TOKEN_ID, UUID.randomUUID().toString()
                )
            )
            Resource.Success(data = token)
        } catch (e: Exception) {
            Resource.Failure(
                status = HttpStatusCode.ExpectationFailed,
                message = e.message,
                origin = this@UserSignInUseCase
            )
        }
    }
}