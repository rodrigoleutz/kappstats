package com.kappstats.presentation.core.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.kappstats.components.navigation.ComposeRoute
import com.kappstats.resources.Res
import com.kappstats.resources.auth
import com.kappstats.resources.home
import com.kappstats.resources.sign_in
import com.kappstats.resources.sign_up
import com.kappstats.resources.splash
import compose.icons.EvaIcons
import compose.icons.FeatherIcons
import compose.icons.Octicons
import compose.icons.TablerIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.LogIn
import compose.icons.feathericons.Home
import compose.icons.octicons.SignIn24
import compose.icons.tablericons.Loader
import compose.icons.tablericons.Registered
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.StringResource

@Serializable
sealed interface AppScreens : ComposeRoute, NavKey {

    companion object {
        val all: List<ComposeRoute> = listOf(Home, Splash)

        val configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Auth::class, Auth.serializer())
                    subclass(Auth.SignIn::class, Auth.SignIn.serializer())
                    subclass(Auth.SignUp::class, Auth.SignUp.serializer())
                    subclass(AppScreens.Home::class, AppScreens.Home.serializer())
                    subclass(Splash::class, Splash.serializer())
                }
            }
        }
    }

    @Serializable
    data object Auth : AppScreens {
        override val title: StringResource = Res.string.auth
        override val icon: ImageVector = TablerIcons.Registered
        override val route: @Serializable Auth = this
        override val subRoutes: Set<ComposeRoute> = setOf(SignIn, SignUp)

        @Serializable
        data object SignIn : AppScreens {
            override val title: StringResource = Res.string.sign_in
            override val icon: ImageVector = EvaIcons.Fill.LogIn
            override val route: @Serializable SignIn = this
        }

        @Serializable
        data object SignUp : AppScreens {
            override val title: StringResource = Res.string.sign_up
            override val icon: ImageVector = Octicons.SignIn24
            override val route: @Serializable SignUp = this
        }
    }


    @Serializable
    data object Home : AppScreens {
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

