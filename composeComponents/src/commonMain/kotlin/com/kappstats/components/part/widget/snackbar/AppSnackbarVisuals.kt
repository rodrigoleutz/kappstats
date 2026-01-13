package com.kappstats.components.part.widget.snackbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.kappstats.components.theme.Gray80
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.AlertCircle
import compose.icons.evaicons.outline.Checkmark
import compose.icons.evaicons.outline.CloseCircle
import compose.icons.evaicons.outline.Info
import compose.icons.evaicons.outline.Settings

data class AppSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = true,
    override val duration: SnackbarDuration = SnackbarDuration.Indefinite,
    val type: Type = Type.Info,
    val icon: ImageVector? = null,
    val colors: Colors? = null,
) : SnackbarVisuals {
    enum class Type {
        Debug,
        Error,
        Info,
        Success,
        Warning;
    }

    data class Colors(
        val contentColor: Color = Color.Black,
        val containerColor: Color = Gray80,
    )

    val showColors: Colors
        @Composable
        get() {
            if (colors != null) return colors
            return when (type) {
                Type.Debug -> Colors()
                Type.Error -> Colors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                )
                Type.Info -> Colors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                )
                Type.Success -> Colors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.secondary
                )
                Type.Warning -> Colors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.tertiary
                )
            }
        }

    val showIcon: ImageVector
        get() {
            if (icon != null) return icon
            return when (type) {
                Type.Debug -> EvaIcons.Outline.Settings
                Type.Error -> EvaIcons.Outline.CloseCircle
                Type.Info -> EvaIcons.Outline.Info
                Type.Success -> EvaIcons.Outline.Checkmark
                Type.Warning -> EvaIcons.Outline.AlertCircle
            }
        }
}