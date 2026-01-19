package com.kappstats.components.part.widget.bottom_bar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import com.kappstats.components.navigation.ComposeRoute
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomNavigationBarWidget(
    items: List<ComposeRoute>,
    selected: ComposeRoute,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Transparent,
    contentColor: Color = Color.Black,
    itemColors: NavigationBarItemColors = NavigationBarItemDefaults.colors(),
    onClick: (ComposeRoute) -> Unit
) {
    NavigationBar(
        modifier = Modifier.then(modifier),
        windowInsets = NavigationBarDefaults.windowInsets,
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        items.forEach { item ->
            NavigationBarItem(
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                selected = selected == item,
                label = {
                    Text(stringResource(item.title))
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.title)
                    )
                },
                onClick = {
                    onClick(item)
                },
                colors = itemColors
            )
        }
    }
}