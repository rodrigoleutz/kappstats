package com.kappstats

import android.app.Application
import com.kappstats.util.AndroidContextProvider

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidContextProvider.initialize(this)
    }
}