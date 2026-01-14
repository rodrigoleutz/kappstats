package com.kappstats.data.local.api

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class LocalSettingsApi {
    private val settings: Settings = Settings()

    fun getLong(name: String, default: Long = 0L): Long {
        return settings[name] ?: default
    }

    fun getString(name: String, default: String = ""): String {
        return settings[name] ?: default
    }

    fun setLong(name: String, value: Long) {
        settings[name] = value
    }

    fun setString(name: String, value: String) {
        settings[name] = value
    }


}