package com.kappstats.components.part.widget.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.vector.ImageVector

suspend fun SnackbarHostState.show(
    message: String,
    icon: ImageVector? = null,
    type: AppSnackbarVisuals.Type = AppSnackbarVisuals.Type.Info,
) {
    this.showSnackbar(
        AppSnackbarVisuals(
            message = message,
            icon = icon,
            type = type
        )
    )
}