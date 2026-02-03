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
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
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
    swipeToDismissBoxState: SwipeToDismissBoxState,
    modifier: Modifier = Modifier,
    swipeLeftToRight: () -> Unit,
    swipeRightToLeft: () -> Unit,
    onClick: () -> Unit
) {
    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        backgroundContent = {
            when (swipeToDismissBoxState.settledValue) {
                SwipeToDismissBoxValue.StartToEnd -> swipeLeftToRight()
                SwipeToDismissBoxValue.EndToStart -> swipeRightToLeft()
                else -> {}
            }
            val direction = swipeToDismissBoxState.dismissDirection
            if (direction == SwipeToDismissBoxValue.Settled) return@SwipeToDismissBox
            fun <T> config(edit: T, delete: T): T =
                if (direction == SwipeToDismissBoxValue.StartToEnd) edit else delete
            Card(
                modifier = Modifier.fillMaxSize().padding(AppDimensions.Medium.component)
                    .then(modifier),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = AppDimensions.None.component
                ),
                colors = CardDefaults.cardColors(
                    containerColor = config(Green20, Red20),
                    contentColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = config(Alignment.Start, Alignment.End),
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.padding(AppDimensions.Large.component),
                        imageVector = config(TablerIcons.Edit, TablerIcons.Trash),
                        contentDescription = stringResource(
                            config(
                                Res.string.edit,
                                Res.string.delete
                            )
                        )
                    )
                }
            }
        }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(AppDimensions.Medium.component)
                .pointerHoverIcon(PointerIcon.Hand).then(modifier),
            elevation = CardDefaults.cardElevation(
                defaultElevation = AppDimensions.Medium.component
            ),
            onClick = {
                onClick()
            }
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