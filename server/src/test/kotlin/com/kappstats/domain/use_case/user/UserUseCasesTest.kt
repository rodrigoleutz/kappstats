package com.kappstats.domain.use_case.user

import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.custom_object.username.Username
import com.kappstats.data.remote.api.database.mongo.MongoApi
import com.kappstats.test_util.container.MongoTestContainer
import com.kappstats.data.repository.user.AuthRepository
import com.kappstats.data.repository.user.AuthRepositoryImpl
import com.kappstats.data.repository.user.ProfileRepository
import com.kappstats.data.repository.user.ProfileRepositoryImpl
import com.kappstats.domain.constants.DomainConstants
import com.kappstats.domain.core.security.hashing.HashingService
import com.kappstats.domain.core.security.hashing.SHA256HashingServiceImpl
import com.kappstats.domain.core.security.token.JwtTokenServiceImpl
import com.kappstats.domain.core.security.token.TokenService
import com.kappstats.dto.request.user.SignInRequest
import com.kappstats.dto.request.user.SignUpRequest
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertInstanceOf
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class UserUseCasesTest {

    companion object {
        val signUpRequest = SignUpRequest(
            email = Email("test@test.com"),
            username = Username("test123"),
            name = "Test User",
            password = Password("#Senha123")
        )
        val tokenConfig = DomainConstants.tokenConfig
    }

    private lateinit var authRepository: AuthRepository
    private lateinit var profileRepository: ProfileRepository
    private lateinit var hashingService: HashingService
    private lateinit var tokenService: TokenService
    private lateinit var userUseCases: UserUseCases

    @BeforeEach
    fun setUp() {
        val mongoApi = MongoApi(
            serverUrl = MongoTestContainer.connectionString,
            databaseName = "KAppStatsTest"
        )
        authRepository = AuthRepositoryImpl(mongoApi)
        profileRepository = ProfileRepositoryImpl(mongoApi)
        hashingService = SHA256HashingServiceImpl()
        tokenService = JwtTokenServiceImpl(tokenConfig)
        userUseCases = UserUseCases(
            signIn = UserSignInUseCase(
                authRepository = authRepository,
                hashingService = hashingService,
                tokenService = tokenService
            ),
            signUp = UserSignUpUseCase(
                authRepository = authRepository,
                profileRepository = profileRepository,
                hashingService = hashingService
            )
        )
    }

    @AfterEach
    fun setDown() = runTest {
        clearDatabase()
    }

    private suspend fun MongoCollection<*>.deleteMany() {
        val result = this.deleteMany(Filters.empty())
        println("Deleted documents: ${result.deletedCount}")
    }

    suspend fun clearDatabase() {
        authRepository.generic.database.collection.deleteMany()
        profileRepository.generic.database.collection.deleteMany()
    }

    @Test
    fun `SignUp use case test success`() = runTest {
        val signUp = userUseCases.signUp.invoke(signUpRequest)
        assert(signUp.isSuccess)
        assertEquals(HttpStatusCode.Created, signUp.statusCode)
    }

    @Test
    fun `SignUp use case email failure`() = runTest {
        assertThrows<IllegalArgumentException> {
            userUseCases.signUp.invoke(
                signUpRequest.copy(
                    email = Email("teste@teste")
                )
            )
        }
    }

    @Test
    fun `SignUp use case name blank failure`() = runTest {
        assertThrows<IllegalArgumentException> {
            userUseCases.signUp.invoke(
                signUpRequest.copy(
                    name = ""
                )
            )
        }
    }

    @Test
    fun `SignUp and SignIn test success`() = runTest {
        val signUpRequestTest = signUpRequest.copy(
            email = Email("signtest@test.com"),
            username = Username("signtest13")
        )
        val signUp = userUseCases.signUp.invoke(signUpRequestTest)
        assert(signUp.isSuccess)
        val signInRequest = SignInRequest(
            email = signUpRequestTest.email,
            password = signUpRequestTest.password
        )
        val signIn = userUseCases.signIn.invoke(signInRequest)
        assert(signIn.isSuccess)
        assertInstanceOf<String>(signIn.asDataOrNull)
    }

    @Test
    fun `SignUp failure conflict`() = runTest {
        val signUp = userUseCases.signUp.invoke(signUpRequest)
        assert(signUp.isSuccess)
        val errorSignUp = userUseCases.signUp.invoke(signUpRequest.copy(
            username = Username("hello123")
        ))
        assert(!errorSignUp.isSuccess)
        assertEquals(HttpStatusCode.Conflict, errorSignUp.statusCode)
        val profileList = profileRepository.generic.database.getAllWithLimitAndSkip()
        assertEquals(1, profileList.size)
        val authList = authRepository.generic.database.getAllWithLimitAndSkip()
        assertEquals(1, authList.size)
    }

    @Test
    fun `Decode token service`() = runTest {
        val signUp = userUseCases.signUp.invoke(signUpRequest)
        assert(signUp.isSuccess)
        val signInRequest = SignInRequest(
            email = signUpRequest.email,
            password = signUpRequest.password
        )
        val signIn = userUseCases.signIn.invoke(signInRequest)
        val token = signIn.asDataOrNull
        assert(signIn.isSuccess)
        assertInstanceOf<String>(token)
        val authList = authRepository.generic.database.getAllWithLimitAndSkip()
        assertEquals(1, authList.size)
        val auth = authList.first()
        val decodedToken = tokenService.decode(token)
        assertEquals(auth._id.toHexString(), decodedToken?.get(DomainConstants.AUTH_ID))
        assertEquals(auth.profileId, decodedToken?.get(DomainConstants.PROFILE_ID))
    }

    @Test
    fun `Decode token error`() = runTest {
        val decodedToken = tokenService.decode("kjadlkajkhdkjfhalk")
        assertEquals(null, decodedToken)
    }
}