package com.kappstats.presentation.screen.apps.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kappstats.components.part.component.button.ButtonComponent
import com.kappstats.components.part.component.input.InputTextComponent
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Red20
import com.kappstats.domain.data_state.apps_monitor.AppsMonitorState
import com.kappstats.presentation.core.state.MainUiState
import com.kappstats.presentation.screen.apps.AppsMonitorEvent
import com.kappstats.presentation.screen.apps.AppsMonitorUiState
import com.kappstats.resources.Res
import com.kappstats.resources.add
import com.kappstats.resources.cancel
import com.kappstats.resources.clear
import com.kappstats.resources.description
import com.kappstats.resources.name
import com.kappstats.resources.save
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Close
import compose.icons.evaicons.fill.Save
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppsMonitorSetScreen(
    mainUiState: MainUiState,
    uiState: AppsMonitorUiState,
    appsMonitorState: AppsMonitorState,
    onEvent: (AppsMonitorEvent) -> Unit,
    id: String? = null,
    modifier: Modifier = Modifier,
    onClickCancel: () -> Unit,
    onClickSave: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(mainUiState.paddingValues.calculateTopPadding()))

        Column(
            modifier = Modifier.fillMaxWidth().padding(AppDimensions.Medium.component)
        ) {
            InputTextComponent(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.name),
                value = uiState.name,
                onChange = {
                    onEvent(AppsMonitorEvent.SetName(it))
                }
            )
            InputTextComponent(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.description),
                value = uiState.description,
                onChange = {
                    onEvent(AppsMonitorEvent.SetDescription(it))
                }
            )
            Spacer(modifier = Modifier.height(AppDimensions.ExtraLarge.component))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                ButtonComponent(
                    modifier = Modifier.weight(1f),
                    label = stringResource(if (id.isNullOrBlank()) Res.string.clear else Res.string.cancel),
                    icon = EvaIcons.Fill.Close,
                    onClick = {
                        if (id.isNullOrBlank()) {
                            onEvent(AppsMonitorEvent.SetName(""))
                            onEvent(AppsMonitorEvent.SetDescription(""))
                        } else onClickCancel()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red20,
                        contentColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
                ButtonComponent(
                    modifier = Modifier.weight(1f),
                    label = stringResource(if (id.isNullOrBlank()) Res.string.add else Res.string.save),
                    icon = EvaIcons.Fill.Save,
                    onClick = {
                        onClickSave()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue20,
                        contentColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(mainUiState.paddingValues.calculateBottomPadding()))
    }
}