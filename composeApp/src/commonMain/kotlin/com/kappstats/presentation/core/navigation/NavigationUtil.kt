package com.kappstats.presentation.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.replace(route: NavKey) {
    val lastIndex = this.lastIndex
    if(lastIndex < 0) this.add(route)
    this[lastIndex] = route
}