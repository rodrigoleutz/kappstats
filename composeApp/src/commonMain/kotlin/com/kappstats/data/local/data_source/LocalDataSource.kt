package com.kappstats.data.local.data_source

import com.kappstats.data.local.api.LocalSettingsApi

interface LocalDataSource {

    val settings: LocalSettingsApi
}