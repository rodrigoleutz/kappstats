package com.kappstats.presentation.core.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.kappstats.components.navigation.ComposeRoute
import com.kappstats.resources.Res
import com.kappstats.resources.auth
import com.kappstats.resources.exit
import com.kappstats.resources.home
import com.kappstats.resources.logout
import com.kappstats.resources.privacy_policy
import com.kappstats.resources.privacy_policy_and_terms
import com.kappstats.resources.profile
import com.kappstats.resources.sign_in
import com.kappstats.resources.sign_up
import com.kappstats.resources.splash
import com.kappstats.resources.terms_and_conditions
import compose.icons.EvaIcons
import compose.icons.FeatherIcons
import compose.icons.Octicons
import compose.icons.TablerIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Close
import compose.icons.evaicons.fill.LogIn
import compose.icons.evaicons.fill.LogOut
import compose.icons.evaicons.fill.Person
import compose.icons.evaicons.fill.PersonAdd
import compose.icons.feathericons.Home
import compose.icons.octicons.SignIn24
import compose.icons.tablericons.Loader
import compose.icons.tablericons.Registered
import compose.icons.tablericons.Shield
import compose.icons.tablericons.ShieldCheck
import compose.icons.tablericons.ShieldLock
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.StringResource

@Serializable
sealed interface AppScreens : ComposeRoute, NavKey {

    companion object {
        val all: List<AppScreens> = listOf(
            Home, Splash, Auth.LogOut, Auth.SignIn, Auth.SignUp,
            PrivacyAndTerms, PrivacyAndTerms.PrivacyPolicy, PrivacyAndTerms.TermsAndConditions
        )
        val unlogged: List<AppScreens> = listOf(
            Auth.SignIn,
            Auth.SignUp,
            PrivacyAndTerms.TermsAndConditions,
            PrivacyAndTerms.PrivacyPolicy,
            Exit
        )
        val unloggedDrawerDivider: List<AppScreens> = listOf(PrivacyAndTerms.TermsAndConditions, Exit)
        val logged: List<AppScreens> = listOf(
            AppScreens.Home,
            Profile,
            PrivacyAndTerms.TermsAndConditions,
            PrivacyAndTerms.PrivacyPolicy,
            Auth.LogOut,
            Exit
        )
        val loggedDrawerDivider: List<AppScreens> =
            listOf(Auth.LogOut, PrivacyAndTerms.TermsAndConditions)

        val configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Auth::class, Auth.serializer())
                    subclass(Auth.LogOut::class, Auth.LogOut.serializer())
                    subclass(Auth.SignIn::class, Auth.SignIn.serializer())
                    subclass(Auth.SignUp::class, Auth.SignUp.serializer())
                    subclass(AppScreens.Home::class, AppScreens.Home.serializer())
                    subclass(PrivacyAndTerms::class, PrivacyAndTerms.serializer())
                    subclass(
                        PrivacyAndTerms.PrivacyPolicy::class,
                        PrivacyAndTerms.PrivacyPolicy.serializer()
                    )
                    subclass(
                        PrivacyAndTerms.TermsAndConditions::class,
                        PrivacyAndTerms.TermsAndConditions.serializer()
                    )
                    subclass(Profile::class, Profile.serializer())
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
        data object LogOut : AppScreens {
            override val title: StringResource = Res.string.logout
            override val icon: ImageVector = EvaIcons.Fill.LogOut
            override val route: @Serializable LogOut = this
        }

        @Serializable
        data object SignIn : AppScreens {
            override val title: StringResource = Res.string.sign_in
            override val icon: ImageVector = EvaIcons.Fill.LogIn
            override val route: @Serializable SignIn = this
        }

        @Serializable
        data object SignUp : AppScreens {
            override val title: StringResource = Res.string.sign_up
            override val icon: ImageVector = EvaIcons.Fill.PersonAdd
            override val route: @Serializable SignUp = this
        }
    }

    @Serializable
    data object Exit: AppScreens {
        override val title: StringResource = Res.string.exit
        override val icon: ImageVector = EvaIcons.Fill.Close
        override val route: @Serializable Exit = this
    }

    @Serializable
    data object Home : AppScreens {
        override val title: StringResource = Res.string.home
        override val icon: ImageVector = FeatherIcons.Home
        override val route: @Serializable Home = this
    }

    @Serializable
    data object PrivacyAndTerms : AppScreens {
        override val title: StringResource = Res.string.privacy_policy_and_terms
        override val icon: ImageVector = TablerIcons.Shield
        override val route: @Serializable PrivacyAndTerms = this
        override val subRoutes: Set<ComposeRoute> = setOf(PrivacyPolicy, TermsAndConditions)

        @Serializable
        data object PrivacyPolicy : AppScreens {
            override val title: StringResource = Res.string.privacy_policy
            override val icon: ImageVector = TablerIcons.ShieldLock
            override val route: @Serializable PrivacyPolicy = this
        }

        @Serializable
        data object TermsAndConditions : AppScreens {
            override val title: StringResource = Res.string.terms_and_conditions
            override val icon: ImageVector = TablerIcons.ShieldCheck
            override val route: @Serializable TermsAndConditions = this
        }
    }

    @Serializable
    data object Profile: AppScreens {
        override val title: StringResource = Res.string.profile
        override val icon: ImageVector = EvaIcons.Fill.Person
        override val route: @Serializable Profile = this
    }

    @Serializable
    data object Splash : AppScreens {
        override val title: StringResource = Res.string.splash
        override val icon: ImageVector = TablerIcons.Loader
        override val route: @Serializable Splash = this
    }


}

