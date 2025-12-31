package com.kappstats.components.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import org.jetbrains.compose.resources.StringResource
import kotlin.reflect.KClass

interface ComposeRoute {
    val title: StringResource
    val icon: ImageVector
    val route: @Serializable Any

    val subRoutes: Set<ComposeRoute>
        get() = emptySet()
}