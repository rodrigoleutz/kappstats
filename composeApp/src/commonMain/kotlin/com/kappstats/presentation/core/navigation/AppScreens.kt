package com.kappstats.presentation.core.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.kappstats.components.navigation.ComposeRoute
import com.kappstats.resources.Res
import com.kappstats.resources.add
import com.kappstats.resources.apps_monitor
import com.kappstats.resources.auth
import com.kappstats.resources.dashboard
import com.kappstats.resources.edit
import com.kappstats.resources.exit
import com.kappstats.resources.home
import com.kappstats.resources.logout
import com.kappstats.resources.privacy_policy
import com.kappstats.resources.privacy_policy_and_terms
import com.kappstats.resources.profile
import com.kappstats.resources.settings
import com.kappstats.resources.sign_in
import com.kappstats.resources.sign_up
import com.kappstats.resources.splash
import com.kappstats.resources.terms_and_conditions
import compose.icons.EvaIcons
import compose.icons.FeatherIcons
import compose.icons.TablerIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Close
import compose.icons.evaicons.fill.Email
import compose.icons.evaicons.fill.LogIn
import compose.icons.evaicons.fill.LogOut
import compose.icons.evaicons.fill.Person
import compose.icons.evaicons.fill.PersonAdd
import compose.icons.feathericons.Home
import compose.icons.tablericons.Apps
import compose.icons.tablericons.CodePlus
import compose.icons.tablericons.Dashboard
import compose.icons.tablericons.EditCircle
import compose.icons.tablericons.Loader
import compose.icons.tablericons.Registered
import compose.icons.tablericons.Settings
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
            Home, Splash, Auth.LogOut, Auth.SignIn, Auth.SignUp, Dashboard, Settings,
            PrivacyAndTerms, PrivacyAndTerms.PrivacyPolicy, PrivacyAndTerms.TermsAndConditions,
            Exit
        )
        val gestureDisabled = listOf<AppScreens>(
            Dashboard, PrivacyAndTerms.PrivacyPolicy, PrivacyAndTerms.TermsAndConditions
        )
        val unlogged: List<AppScreens> = listOf(
            Auth.SignIn,
            Auth.SignUp,
            PrivacyAndTerms.TermsAndConditions,
            PrivacyAndTerms.PrivacyPolicy,
            Exit
        )
        val unloggedDrawerDivider: List<AppScreens> =
            listOf(PrivacyAndTerms.TermsAndConditions, Exit)
        val logged: List<AppScreens> = listOf(
            AppScreens.Home,
            AppsMonitor,
            Dashboard,
            Settings,
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
                    subclass(AppsMonitor::class, AppsMonitor.serializer())
                    subclass(AppsMonitor.Add::class, AppsMonitor.Add.serializer())
                    subclass(AppsMonitor.Edit::class, AppsMonitor.Edit.serializer())
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
    data object AppsMonitor : AppScreens {
        override val title: StringResource = Res.string.apps_monitor
        override val icon: ImageVector = TablerIcons.Apps
        override val route: @Serializable AppsMonitor = this
        override val subRoutes: Set<ComposeRoute> = setOf(Add)

        @Serializable
        data object Add : AppScreens {
            override val title: StringResource = Res.string.add
            override val icon: ImageVector = TablerIcons.CodePlus
            override val route: @Serializable Add = this
        }

        @Serializable
        data class Edit(val id: String) : AppScreens {
            override val title: StringResource
                get() = Res.string.edit
            override val icon: ImageVector
                get() = TablerIcons.EditCircle
            override val route: @Serializable Edit = this
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
    data object Dashboard : AppScreens {
        override val title: StringResource = Res.string.dashboard
        override val icon: ImageVector = TablerIcons.Dashboard
        override val route: @Serializable Dashboard = this
    }

    @Serializable
    data object Exit : AppScreens {
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
    data object Profile : AppScreens {
        override val title: StringResource = Res.string.profile
        override val icon: ImageVector = EvaIcons.Fill.Person
        override val route: @Serializable Profile = this
        override val subRoutes: Set<ComposeRoute> = setOf(ProfileProfile, ProfileAuth)

        data object ProfileProfile : AppScreens {
            override val title: StringResource = Res.string.profile
            override val icon: ImageVector = EvaIcons.Fill.Person
            override val route: @Serializable ProfileProfile = this
            override val bottomBar: List<ComposeRoute> = listOf(ProfileProfile, ProfileAuth)
        }

        data object ProfileAuth : AppScreens {
            override val title: StringResource = Res.string.auth
            override val icon: ImageVector = EvaIcons.Fill.Email
            override val route: @Serializable ProfileAuth = this
            override val bottomBar: List<ComposeRoute> = listOf(ProfileProfile, ProfileAuth)
        }
    }

    @Serializable
    data object Settings : AppScreens {
        override val title: StringResource = Res.string.settings
        override val icon: ImageVector = TablerIcons.Settings
        override val route: @Serializable Settings = this
    }

    @Serializable
    data object Splash : AppScreens {
        override val title: StringResource = Res.string.splash
        override val icon: ImageVector = TablerIcons.Loader
        override val route: @Serializable Splash = this
    }


}

