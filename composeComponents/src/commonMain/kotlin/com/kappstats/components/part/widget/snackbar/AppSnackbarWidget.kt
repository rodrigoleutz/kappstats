package com.kappstats.components.part.widget.snackbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kappstats.components.theme.AppDimensions
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Close

@Composable
fun AppSnackbarWidget(
    snackbarHostState: SnackbarHostState,
    closeDescription: String = "Close"
) {
    SnackbarHost(snackbarHostState) { data ->
        val custom = data.visuals as AppSnackbarVisuals
        Snackbar(
            modifier = Modifier
                .padding(AppDimensions.Medium.component),
            containerColor = custom.showColors.containerColor,
            contentColor = custom.showColors.contentColor,
            action = {
                data.visuals.actionLabel?.let { label ->
                    TextButton(
                        onClick = {
                            data.performAction()
                        }
                    ) {
                        Text(label)
                    }
                }
            },
            dismissAction = {
                if (data.visuals.withDismissAction) {
                    IconButton(
                        onClick = {
                            data.dismiss()
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = custom.showColors.contentColor
                        )
                    ) {
                        Icon(
                            imageVector = EvaIcons.Outline.Close,
                            contentDescription = closeDescription
                        )
                    }
                }
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = custom.showIcon,
                    contentDescription = custom.type.name
                )
                Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
                Text(custom.message)
            }
        }
    }
}