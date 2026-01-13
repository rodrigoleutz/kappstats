package com.kappstats.presentation.screen.privacy_and_terms.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.kappstats.presentation.core.navigation.AppScreens
import com.kappstats.presentation.core.state.MainStateHolder
import com.kappstats.presentation.screen.privacy_and_terms.screen.PrivacyPolicyScreen
import com.kappstats.presentation.screen.privacy_and_terms.screen.TermsAndConditionsScreen

fun EntryProviderScope<NavKey>.privacyAndTermsNavigation(
    navBackStack: NavBackStack<NavKey>,
    stateHolder: MainStateHolder
) {
    entry<AppScreens.PrivacyAndTerms.PrivacyPolicy> {
        val mainUiState by stateHolder.uiState.collectAsState()
        PrivacyPolicyScreen(paddingValues = mainUiState.paddingValues)
    }
    entry<AppScreens.PrivacyAndTerms.TermsAndConditions> {
        val mainUiState by stateHolder.uiState.collectAsState()
        TermsAndConditionsScreen(paddingValues = mainUiState.paddingValues)
    }
}