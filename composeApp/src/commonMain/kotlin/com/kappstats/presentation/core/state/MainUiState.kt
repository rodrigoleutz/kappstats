package com.kappstats.presentation.core.state

import androidx.compose.foundation.layout.PaddingValues

data class MainUiState(
    val hasTopBar: Boolean = false,
    val isBackButton: Boolean = false,
    val paddingValues: PaddingValues = PaddingValues(),
    val title: String = ""
)
