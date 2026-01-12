package com.kappstats.data.remote.data_source

import io.ktor.client.HttpClient

interface RemoteDataSource {
    val client: HttpClient

}