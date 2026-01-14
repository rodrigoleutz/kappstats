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
        stateHolder.onMainEvent(MainEvent.SetIsLoading(true))
        viewModelScope.launch {
            val resource = authUseCases.authenticate.invoke()
            stateHolder.onMainEvent(MainEvent.SetIsLogged(resource.isSuccess))
            stateHolder.onMainEvent(MainEvent.SetIsLoading(false))
            result(resource.isSuccess)
        }
    }
}