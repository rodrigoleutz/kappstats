package com.kappstats.presentation.util

import android.app.Activity
import com.kappstats.util.AndroidContextProvider

actual fun exitApplication() {
    val context = AndroidContextProvider.get()
    (context as Activity).finish()
}