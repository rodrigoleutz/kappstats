package com.kappstats.components.part.widget.drawer_menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kappstats.components.navigation.ComposeRoute
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Blue40

@Composable
fun DrawerMenuWidget(
    itemList: List<ComposeRoute>,
    selected: ComposeRoute,
    dividerBefore: List<ComposeRoute> = emptyList(),
    drawerCard: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    colors: DrawerMenuWidgetColors = DrawerMenuWidgetColors(),
    onClick: (ComposeRoute) -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier,
        windowInsets = WindowInsets(
            top = AppDimensions.None.component,
            bottom = AppDimensions.None.component
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(colors.containerColor).then(modifier)
                .verticalScroll(rememberScrollState())
        ) {
            drawerCard?.let {
                Box(
                    modifier = Modifier.fillMaxWidth().height(AppDimensions.Large.image),
                    contentAlignment = Alignment.Center
                ) {
                    drawerCard()
                }
            }
            Spacer(modifier = Modifier)
            itemList.forEach { item ->
                if (item in dividerBefore)
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Blue40
                    )
                DrawerMenuItemWidget(
                    item = item,
                    selected = item == selected,
                    colors = colors,
                    onClick = {
                        onClick(item)
                    }
                )
                if (item.subRoutes.isNotEmpty()) {
                    if (selected in item.subRoutes) {
                        item.subRoutes.forEach { subRoute ->
                            DrawerMenuItemWidget(
                                item = subRoute,
                                selected = subRoute == selected,
                                colors = colors,
                                onClick = {
                                    onClick(subRoute)
                                },
                                isSubRoute = true
                            )
                        }
                    }
                }
            }
        }
    }
}