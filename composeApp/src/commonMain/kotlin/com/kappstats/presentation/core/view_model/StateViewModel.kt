package com.kappstats.presentation.core.view_model

import androidx.lifecycle.ViewModel
import com.kappstats.presentation.core.state.MainEvent
import com.kappstats.presentation.core.state.MainStateHolder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class StateViewModel(): ViewModel(), KoinComponent {
    val stateHolder by inject<MainStateHolder>()
}