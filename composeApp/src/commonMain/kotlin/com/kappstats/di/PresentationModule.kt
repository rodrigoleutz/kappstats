package com.kappstats.di

import com.kappstats.presentation.core.state.MainStateHolder
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val presentationModule = module {
    singleOf(::MainStateHolder)
}