package com.kappstats.presentation.core.state

import androidx.compose.foundation.layout.PaddingValues
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet

class MainStateHolder {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    fun setHasTopBar(value: Boolean): Boolean =
        _uiState.updateAndGet { it.copy(hasTopBar = value) }.hasTopBar

    fun setIsBackButton(value: Boolean): Boolean =
        _uiState.updateAndGet { it.copy(isBackButton = value) }.isBackButton

    fun setPaddingValues(value: PaddingValues): PaddingValues =
        _uiState.updateAndGet { it.copy(paddingValues = value) }.paddingValues

    fun setTitle(value: String): String = _uiState.updateAndGet { it.copy(title = value) }.title

}