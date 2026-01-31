package com.kappstats.presentation.screen.apps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Dialog
import com.kappstats.components.part.component.card.CardMessageComponent
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Orange20
import com.kappstats.domain.data_state.apps_monitor.AppsMonitorState
import com.kappstats.presentation.core.state.MainUiState
import com.kappstats.presentation.screen.apps.part.AppMonitorItemPart
import com.kappstats.presentation.screen.apps.part.AppsMonitorDialogItemPart
import com.kappstats.resources.Res
import com.kappstats.resources.add
import com.kappstats.resources.empty_list
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Plus
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppsMonitorScreen(
    mainUiState: MainUiState,
    uiState: AppsMonitorUiState,
    appsMonitorState: AppsMonitorState,
    onEvent: (AppsMonitorEvent) -> Unit,
    onClickAdd: () -> Unit,
    onEdit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var openDialog: Pair<Boolean, String?> by remember { mutableStateOf(false to null) }
    var isEdit by remember { mutableStateOf(false) }
    var currentSwipeToDismissBoxState: SwipeToDismissBoxState? = null
    val scope = rememberCoroutineScope()
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (appsMonitorState.mapAppsMonitor.values.toList().isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CardMessageComponent(
                    message = stringResource(Res.string.empty_list)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                contentPadding = mainUiState.paddingValues
            ) {
                items(appsMonitorState.mapAppsMonitor.values.toList()) { item ->
                    val swipeToDismissBoxState = rememberSwipeToDismissBoxState()
                    AppMonitorItemPart(
                        modifier = Modifier,
                        swipeToDismissBoxState = swipeToDismissBoxState,
                        appMonitor = item,
                        swipeLeftToRight = {
                            isEdit = true
                            openDialog = true to item.id
                            currentSwipeToDismissBoxState = swipeToDismissBoxState
                        },
                        swipeRightToLeft = {
                            isEdit = false
                            openDialog = true to item.id
                            currentSwipeToDismissBoxState = swipeToDismissBoxState
                        }
                    )
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(
                    bottom = mainUiState.paddingValues.calculateBottomPadding() + AppDimensions.ExtraLarge.component,
                    end = mainUiState.paddingValues.calculateEndPadding(LayoutDirection.Ltr) + AppDimensions.ExtraLarge.component
                ),
            onClick = {
                onClickAdd()
            },
            containerColor = Orange20,
            contentColor = Blue20
        ) {
            Icon(
                imageVector = EvaIcons.Fill.Plus,
                contentDescription = stringResource(Res.string.add)
            )
        }
        AnimatedVisibility(
            openDialog.first,
        ) {
            val item = appsMonitorState.mapAppsMonitor[openDialog.second]
            item?.let {
                Dialog(
                    onDismissRequest = {
                        scope.launch {
                            currentSwipeToDismissBoxState?.reset()
                            currentSwipeToDismissBoxState = null
                            openDialog = false to null
                        }
                    }
                ) {
                    AppsMonitorDialogItemPart(
                        modifier = Modifier.align(Alignment.Center),
                        appMonitor = item,
                        isEdit = isEdit,
                        onConfirm = {
                            currentSwipeToDismissBoxState?.let { swipe ->
                                scope.launch {
                                    swipe.reset()
                                    openDialog = false to null
                                    currentSwipeToDismissBoxState = null
                                    if (isEdit) {
                                        onEdit(item.id)
                                    } else {
                                        onEvent(AppsMonitorEvent.Delete(item.id))
                                    }
                                }
                            }
                        },
                        onCancel = {
                            currentSwipeToDismissBoxState?.let { swipe ->
                                scope.launch {
                                    swipe.reset()
                                    openDialog = false to null
                                    currentSwipeToDismissBoxState = null
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
