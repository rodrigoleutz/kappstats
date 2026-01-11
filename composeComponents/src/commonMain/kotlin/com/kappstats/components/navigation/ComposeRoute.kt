package com.kappstats.components.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

interface ComposeRoute {
    val title: StringResource
    val icon: ImageVector
    val route: @Serializable Any

    val subRoutes: Set<ComposeRoute>
        get() = emptySet()
}