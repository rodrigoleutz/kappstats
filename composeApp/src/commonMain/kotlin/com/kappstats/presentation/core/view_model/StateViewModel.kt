package com.kappstats.presentation.core.view_model

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstats.components.part.widget.snackbar.AppSnackbarVisuals
import com.kappstats.components.part.widget.snackbar.show
import com.kappstats.presentation.core.state.MainEvent
import com.kappstats.presentation.core.state.MainStateHolder
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class StateViewModel() : ViewModel(), KoinComponent {
    val stateHolder by inject<MainStateHolder>()

    fun snackbarMessage(
        message: String,
        type: AppSnackbarVisuals.Type = AppSnackbarVisuals.Type.Error,
        icon: ImageVector? = null
    ) {
        viewModelScope.launch {
            stateHolder.uiState.value.snackbarHostState.show(
                message = message,
                icon = icon,
                type = type
            )
        }
    }
}