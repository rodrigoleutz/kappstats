package com.kappstats.presentation.screen.apps.part

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Green20
import com.kappstats.components.theme.Red20
import com.kappstats.model.app.AppMonitor
import com.kappstats.resources.Res
import com.kappstats.resources.delete
import com.kappstats.resources.edit
import com.kappstats.resources.error_unknown
import compose.icons.TablerIcons
import compose.icons.tablericons.Edit
import compose.icons.tablericons.Trash
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppMonitorItemPart(
    appMonitor: AppMonitor,
    modifier: Modifier = Modifier,
    swipeLeftToRight: (AppMonitor) -> Unit,
    swipeRightToLeft: (AppMonitor) -> Unit
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState()
    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        backgroundContent = {
            val swipe: Boolean? = when (swipeToDismissBoxState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> true
                SwipeToDismissBoxValue.EndToStart -> false
                SwipeToDismissBoxValue.Settled -> null
            }
            val alignment = when (swipe) {
                true -> Alignment.Start
                false -> Alignment.End
                else -> Alignment.CenterHorizontally
            }
            val icon = when (swipe) {
                true -> TablerIcons.Edit
                false -> TablerIcons.Trash
                else -> null
            }
            val contentDescription = when (swipe) {
                true -> Res.string.edit
                false -> Res.string.delete
                else -> Res.string.error_unknown
            }
            val containerColor = when (swipe) {
                true -> Green20
                false -> Red20
                else -> Color.Transparent
            }
            val contentColor = when (swipe) {
                true -> Color.White
                false -> Color.White
                else -> Color.Transparent
            }
            swipe?.let {
                Card(
                    modifier = Modifier.fillMaxSize().padding(AppDimensions.Medium.component),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = AppDimensions.None.component
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = containerColor,
                        contentColor = contentColor
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = alignment,
                        verticalArrangement = Arrangement.Center
                    ) {
                        icon?.let {
                            Icon(
                                modifier = Modifier.padding(AppDimensions.Large.component),
                                imageVector = icon,
                                contentDescription = stringResource(contentDescription)
                            )
                        }
                    }

                }
            }
        }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(AppDimensions.Medium.component),
            elevation = CardDefaults.cardElevation(
                defaultElevation = AppDimensions.Medium.component
            )
        ) {
            Column(
                modifier = Modifier.padding(AppDimensions.Medium.component)
            ) {
                Text(
                    text = appMonitor.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = appMonitor.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}