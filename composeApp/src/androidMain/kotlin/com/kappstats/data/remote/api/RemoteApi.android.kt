package com.kappstats.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO


class AndroidRemoteApiImpl: RemoteApi {
    override val client: HttpClient = HttpClient(CIO)
}

actual fun getRemoteApi(): RemoteApi = AndroidRemoteApiImpl()