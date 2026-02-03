package com.kappstats.presentation.screen.ai_assistant

import com.kappstats.presentation.core.view_model.StateViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AiAssistantViewModel: StateViewModel() {


    private val _uiState = MutableStateFlow(AiAssistantUiState())
    val uiState = _uiState.asStateFlow()


}