package com.kappstats.components.part.widget.top_bar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Menu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWidget(
    title: String,
    modifier: Modifier = Modifier,
    modifierNavIcon: Modifier = Modifier,
    navigationIcon: ImageVector = EvaIcons.Fill.Menu,
    navigationDescription: String = "menu",
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    actions: @Composable RowScope.() -> Unit = {},
    onNavigationClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(
                modifier = modifierNavIcon.pointerHoverIcon(PointerIcon.Hand).then(modifierNavIcon),
                onClick = {
                    onNavigationClick()
                }
            ) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationDescription
                )
            }
        },
        actions = actions,
        colors = colors
    )
}