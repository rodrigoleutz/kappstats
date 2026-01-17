package com.kappstats.presentation.screen.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.kappstats.components.part.component.button.ButtonComponent
import com.kappstats.components.part.component.container.ScrollableContainerComponent
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Red20
import com.kappstats.presentation.core.state.MainUiState
import com.kappstats.resources.Res
import com.kappstats.resources.cancel
import com.kappstats.resources.ok
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Checkmark
import compose.icons.evaicons.fill.Close
import org.jetbrains.compose.resources.stringResource

@Composable
fun MessageScreen(
    mainUiState: MainUiState,
    modifier: Modifier = Modifier,
    isSingleButton: Boolean = false,
    denyLabel: String = stringResource(Res.string.cancel),
    confirmLabel: String = stringResource(Res.string.ok),
    denyIcon: ImageVector = EvaIcons.Fill.Close,
    confirmIcon: ImageVector = EvaIcons.Fill.Checkmark,
    onDenyClick: () -> Unit = {},
    onConfirmClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppDimensions.Medium.component)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(mainUiState.paddingValues.calculateTopPadding()))
        content()
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (!isSingleButton) {
                ButtonComponent(
                    modifier = Modifier.weight(1f),
                    icon = denyIcon,
                    label = denyLabel,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red20,
                        contentColor = Color.White
                    )
                ) {
                    onDenyClick()
                }
                Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
            }
            ButtonComponent(
                modifier = Modifier.weight(1f),
                icon = confirmIcon,
                label = confirmLabel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue20,
                    contentColor = Color.White
                )
            ) {
                onConfirmClick()
            }
        }
    }
}
