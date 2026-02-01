package com.kappstats.presentation.screen.apps

import androidx.lifecycle.viewModelScope
import com.kappstats.components.part.widget.snackbar.AppSnackbarVisuals
import com.kappstats.domain.web_socket.actions.apps_monitor.AppsMonitorDeleteAction
import com.kappstats.domain.web_socket.actions.apps_monitor.AppsMonitorAddAction
import com.kappstats.domain.web_socket.actions.apps_monitor.AppsMonitorUpdateAction
import com.kappstats.model.app.AppMonitor
import com.kappstats.presentation.core.view_model.StateViewModel
import com.kappstats.resources.Res
import com.kappstats.resources.failure_delete
import com.kappstats.resources.failure_load_data
import com.kappstats.resources.failure_save_data
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

class AppsMonitorViewModel : StateViewModel() {

    val appsMonitorState = stateHolder.dataState.appsMonitor
    private val _uiState = MutableStateFlow(AppsMonitorUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: AppsMonitorEvent) {
        when (event) {

            is AppsMonitorEvent.SetDescription -> {
                _uiState.update { it.copy(description = event.description) }
            }

            is AppsMonitorEvent.SetName -> {
                _uiState.update { it.copy(name = event.name) }
            }

            is AppsMonitorEvent.Delete -> {
                delete(event.id)
            }
        }
    }

    fun add(result: (Boolean) -> Unit) {
        viewModelScope.launch {
            val appMonitor = AppMonitor(
                id = "",
                owner = "",
                name = uiState.value.name,
                description = uiState.value.description,
            )
            val request = AppsMonitorAddAction.send(appMonitor)
            if (request == null) {
                snackbarMessage(
                    message = getString(Res.string.failure_save_data),
                    type = AppSnackbarVisuals.Type.Error
                )
            }
            result(request != null)
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            val response = AppsMonitorDeleteAction.send(id)
            if (response.isNullOrBlank())
                viewModelScope.launch {
                    snackbarMessage(
                        message = getString(Res.string.failure_delete),
                        type = AppSnackbarVisuals.Type.Error
                    )
                }
        }
    }

    fun loadEdit(id: String) {
        viewModelScope.launch {
            val appMonitor = stateHolder.dataState.appsMonitor.value.mapAppsMonitor[id]
                ?: run {
                    snackbarMessage(
                        message = getString(Res.string.failure_load_data),
                        type = AppSnackbarVisuals.Type.Error
                    )
                    return@launch
                }
            _uiState.update {
                it.copy(
                    editAppMonitor = appMonitor,
                    name = appMonitor.name,
                    description = appMonitor.description,
                    members = appMonitor.members
                )
            }
        }
    }

    fun update(result: (Boolean) -> Unit) {
        viewModelScope.launch {
            uiState.value.editAppMonitor?.let { edit ->
                val appMonitorUpdate = edit.copy(
                    name = uiState.value.name,
                    description = uiState.value.description,
                    members = uiState.value.members
                )
                val update = AppsMonitorUpdateAction.send(appMonitorUpdate)
                if (update == null) snackbarMessage(
                    message = getString(Res.string.failure_save_data),
                    type = AppSnackbarVisuals.Type.Error
                )
                result(update != null)
            }
        }
    }

}