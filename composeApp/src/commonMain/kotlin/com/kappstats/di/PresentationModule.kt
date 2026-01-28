package com.kappstats.di

import com.kappstats.presentation.core.state.MainStateHolder
import com.kappstats.presentation.core.view_model.StateViewModel
import com.kappstats.presentation.screen.auth.SignViewModel
import com.kappstats.presentation.screen.dashboard.DashboardViewModel
import com.kappstats.presentation.screen.home.HomeViewModel
import com.kappstats.presentation.screen.profile.ProfileViewModel
import com.kappstats.presentation.screen.splash.SplashViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    singleOf(::MainStateHolder)
    viewModel { SplashViewModel(get()) }
    viewModel { HomeViewModel() }
    viewModel { SignViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
}