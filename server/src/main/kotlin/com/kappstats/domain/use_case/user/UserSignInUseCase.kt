package com.kappstats.domain.use_case.user

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.data.repository.user.AuthRepository
import com.kappstats.data.repository.user.AuthTokenRepository
import com.kappstats.domain.constants.DomainConstants
import com.kappstats.domain.core.resource.Resource
import com.kappstats.domain.core.security.hashing.HashingService
import com.kappstats.domain.core.security.token.TokenClaim
import com.kappstats.domain.core.security.token.TokenService
import com.kappstats.dto.request.user.SignInRequest
import com.kappstats.model.user.Auth
import com.kappstats.model.user.AuthToken
import com.kappstats.model.user.PlatformData
import com.kappstats.model.user.TokenData
import io.ktor.http.HttpStatusCode
import java.util.UUID

class UserSignInUseCase(
    private val authRepository: AuthRepository,
    private val authTokenRepository: AuthTokenRepository,
    private val hashingService: HashingService,
    private val tokenService: TokenService
) {
    suspend operator fun invoke(signInRequest: SignInRequest): Resource<String> {
        return try {
            val authAndSaltedHash = authRepository.getAuthAndSaltedHashByEmail(signInRequest.email)
                ?: return Resource.Failure(
                    status = HttpStatusCode.Unauthorized,
                    message = "User not exists.",
                    origin = this@UserSignInUseCase
                )
            if (!hashingService.verifyHash(
                    signInRequest.password.asString,
                    authAndSaltedHash.second
                )
            ) return Resource.Failure(
                status = HttpStatusCode.Unauthorized,
                message = "Unauthorized verify salted hash.",
                origin = this@UserSignInUseCase
            )
            val tokenId = UUID.randomUUID().toString()
            val token = generateToken(authAndSaltedHash.first, tokenId)
            if (!saveToken(authAndSaltedHash.first, tokenId, signInRequest.platform))
                return Resource.Failure(HttpStatusCode.Locked)
            Resource.Success(data = token)
        } catch (e: Exception) {
            Resource.Failure(
                status = HttpStatusCode.ExpectationFailed,
                message = e.message,
                origin = this@UserSignInUseCase
            )
        }
    }

    private fun generateToken(auth: Auth, tokenId: String): String {
        return tokenService.generate(
            TokenClaim(DomainConstants.AUTH_ID, auth.id),
            TokenClaim(DomainConstants.PROFILE_ID, auth.profileId),
            TokenClaim(DomainConstants.TOKEN_ID, tokenId)
        )
    }

    private suspend fun saveToken(auth: Auth, tokenId: String, platform: PlatformData): Boolean {
        return try {
            val tokenData = TokenData(
                id = tokenId,
                platform = platform,
                isActive = true
            )
            val authToken = authTokenRepository.getByAuthId(auth.id) ?: run {
                return authTokenRepository.generic.add(
                    AuthToken(
                        id = "",
                        authId = auth.id,
                        tokens = listOf(tokenData)
                    )
                ) != null
            }
            authTokenRepository.generic.update(
                authToken.copy(
                    tokens = authToken.tokens + listOf(tokenData),
                    updatedAt = authToken.updatedAt + listOf(AppDateTime.now)
                )
            ) != null
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}