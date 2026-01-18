package com.kappstats.presentation.screen.auth

import com.kappstats.data.repository.auth_token.AuthTokenRepository
import com.kappstats.data.service.auth.AuthService
import com.kappstats.di.dataModule
import com.kappstats.di.domainModule
import com.kappstats.di.presentationModule
import com.kappstats.domain.use_case.auth.AuthAuthenticateUseCase
import com.kappstats.domain.use_case.auth.AuthHasUsernameUseCase
import com.kappstats.domain.use_case.auth.AuthLogoutUseCase
import com.kappstats.domain.use_case.auth.AuthSignInUseCase
import com.kappstats.domain.use_case.auth.AuthSignUpUseCase
import com.kappstats.domain.use_case.auth.AuthUseCases
import com.kappstats.domain.web_socket.WebSocketActions
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SignViewModelTest {

    private lateinit var authService: AuthService
    private lateinit var authTokenRepository: AuthTokenRepository
    private lateinit var authUseCases: AuthUseCases
    private lateinit var webSocketActions: WebSocketActions
    private lateinit var viewModel: SignViewModel

    private fun setMocks(
        signInAuthService: String? = "acbd",
        signUpAuthService: Boolean = true
    ) {
        everySuspend { authService.signIn(any()) } returns signInAuthService
        everySuspend { authService.signUp(any()) } returns signUpAuthService
    }

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(dataModule, domainModule, presentationModule)
        }
        authService = mock()
        authTokenRepository = mock()
        webSocketActions = mock()
        authUseCases = AuthUseCases(
            authenticate = AuthAuthenticateUseCase(
                authService,
                authTokenRepository,
                webSocketActions
            ),
            hasUsername = AuthHasUsernameUseCase(authService),
            logout = AuthLogoutUseCase(authTokenRepository),
            signIn = AuthSignInUseCase(
                authService, authTokenRepository
            ),
            signUp = AuthSignUpUseCase(authService)
        )
        viewModel = SignViewModel(authUseCases)
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `Test sign in success`() = runTest {
        setMocks()
        viewModel.onEvent(SignEvent.SetEmail("test@test.com"))
        viewModel.onEvent(SignEvent.SetPassword("Password#123"))
        viewModel.signIn { result ->
            assertEquals(true, result)
        }
    }

    @Test
    fun `Test sign in failure`() = runTest {
        setMocks(signInAuthService = null)
        viewModel.onEvent(SignEvent.SetEmail("test@test.com"))
        viewModel.onEvent(SignEvent.SetPassword("Password#123"))
        viewModel.signIn { result ->
            assertEquals(false, result)
        }
    }

    @Test
    fun `Test sign in fail illegal argument`() = runTest {
        viewModel.onEvent(SignEvent.SetEmail("test@test"))
        viewModel.onEvent(SignEvent.SetPassword("Password#123"))
        viewModel.signIn { result ->
            assertEquals(false, result)
        }
    }

    @Test
    fun `Test sign up success`() = runTest {
        setMocks()
        viewModel.onEvent(SignEvent.SetName("Test Name"))
        viewModel.onEvent(SignEvent.SetUsername("test123"))
        viewModel.onEvent(SignEvent.SetEmail("test@test.com"))
        viewModel.onEvent(SignEvent.SetPassword("Password#123"))
        viewModel.signUp { result ->
            assertEquals(true, result)
        }
    }

    @Test
    fun `Test sign up failure`() = runTest {
        setMocks(signUpAuthService = false)
        viewModel.onEvent(SignEvent.SetName("Test Name"))
        viewModel.onEvent(SignEvent.SetUsername("test123"))
        viewModel.onEvent(SignEvent.SetEmail("test@test.com"))
        viewModel.onEvent(SignEvent.SetPassword("Password#123"))
        viewModel.signUp { result ->
            assertEquals(false, result)
        }
    }

    @Test
    fun `Test sign up fail illegal argument`() = runTest {
        setMocks()
        viewModel.onEvent(SignEvent.SetName("Test Name"))
        viewModel.onEvent(SignEvent.SetUsername("tst"))
        viewModel.onEvent(SignEvent.SetEmail("test@tes"))
        viewModel.onEvent(SignEvent.SetPassword("Passw"))
        viewModel.signUp { result ->
            assertEquals(false, result)
        }
    }
}