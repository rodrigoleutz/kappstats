package com.kappstats.presentation.core.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.kappstats.components.navigation.ComposeRoute
import com.kappstats.resources.Res
import com.kappstats.resources.home
import com.kappstats.resources.splash
import compose.icons.FeatherIcons
import compose.icons.TablerIcons
import compose.icons.feathericons.Home
import compose.icons.tablericons.Loader
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.StringResource
import kotlin.reflect.KClass

@Serializable
sealed interface AppScreens : NavKey, ComposeRoute {

    companion object {
        val all: List<ComposeRoute> = listOf(Home, Splash)

        val configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(AppScreens.Home::class, AppScreens.Home.serializer())
                    subclass(Splash::class, Splash.serializer())
                }
            }
        }
    }

    @Serializable
    data object Home: AppScreens {
        override val title: StringResource = Res.string.home
        override val icon: ImageVector = FeatherIcons.Home
        override val route: @Serializable Home = this
    }

    @Serializable
    data object Splash : AppScreens {
        override val title: StringResource = Res.string.splash
        override val icon: ImageVector = TablerIcons.Loader
        override val route: @Serializable Splash = this
    }



}

