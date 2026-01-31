package com.kappstats.presentation.screen.apps.part

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kappstats.components.part.component.button.ButtonComponent
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Red20
import com.kappstats.model.app.AppMonitor
import com.kappstats.resources.Res
import com.kappstats.resources.cancel
import com.kappstats.resources.delete
import com.kappstats.resources.delete_app_monitor
import com.kappstats.resources.edit
import com.kappstats.resources.edit_app_monitor
import compose.icons.EvaIcons
import compose.icons.TablerIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Close
import compose.icons.tablericons.Edit
import compose.icons.tablericons.Trash
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppsMonitorDialogItemPart(
    appMonitor: AppMonitor,
    isEdit: Boolean,
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    val text = stringResource(if (isEdit) Res.string.edit else Res.string.delete)
    val icon = if (isEdit) TablerIcons.Edit else TablerIcons.Trash
    Card(
        modifier = modifier.padding(AppDimensions.Large.component),
        elevation = CardDefaults.cardElevation(
            defaultElevation = AppDimensions.Medium.component
        )
    ) {
        Column {
            Row(
                modifier = Modifier.background(Blue20).fillMaxWidth()
                    .padding(AppDimensions.Large.component),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            }
            Column(
                modifier = Modifier.padding(AppDimensions.Medium.component),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier.padding(AppDimensions.Large.component),
                    text = stringResource(
                        if (isEdit) Res.string.edit_app_monitor else Res.string.delete_app_monitor,
                        appMonitor.name
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
                Row {
                    ButtonComponent(
                        modifier = Modifier.weight(1f),
                        label = stringResource(Res.string.cancel),
                        icon = EvaIcons.Fill.Close,
                        onClick = {
                            onCancel()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Red20,
                            contentColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
                    ButtonComponent(
                        modifier = Modifier.weight(1f),
                        label = text,
                        icon = icon,
                        onClick = {
                            onConfirm()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue20,
                            contentColor = Color.White
                        )
                    )
                }
            }
        }
    }
}
