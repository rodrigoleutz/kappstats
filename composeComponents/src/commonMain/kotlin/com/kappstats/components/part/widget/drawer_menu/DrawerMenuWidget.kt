package com.kappstats.components.part.widget.drawer_menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kappstats.components.navigation.ComposeRoute
import com.kappstats.components.theme.AppDimensions

@Composable
fun DrawerMenuWidget(
    itemList: List<ComposeRoute>,
    drawerCard: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (ComposeRoute) -> Unit
) {
    ModalDrawerSheet(

    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(AppDimensions.Large.image)
            ) {
                drawerCard()
            }
        }
    }
}