package com.kappstats.presentation.screen.splash

import androidx.lifecycle.viewModelScope
import com.kappstats.domain.use_case.auth.AuthUseCases
import com.kappstats.presentation.core.state.MainEvent
import com.kappstats.presentation.core.view_model.StateViewModel
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authUseCases: AuthUseCases
): StateViewModel() {

    fun authenticate(result: (Boolean) -> Unit) {
        viewModelScope.launch {
            val resource = authUseCases.authenticate.invoke()
            stateHolder.onMainEvent(MainEvent.SetIsLogged(resource.isSuccess))
            result(resource.isSuccess)
        }
    }
}