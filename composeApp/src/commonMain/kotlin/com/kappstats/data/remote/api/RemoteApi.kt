package com.kappstats.data.remote.api

import io.ktor.client.HttpClient

interface RemoteApi {
    val client: HttpClient
}

expect fun getRemoteApi(): RemoteApi


