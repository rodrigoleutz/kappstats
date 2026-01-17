package com.kappstats.util

import android.content.Context
import java.lang.ref.WeakReference

object AndroidContextProvider {
    private var contextRef: WeakReference<Context>? = null

    fun initialize(context: Context) {
        contextRef = WeakReference(context.applicationContext)
    }

    fun get(): Context = contextRef?.get()
        ?: throw IllegalStateException("Context error in Application.onCreate()")
}