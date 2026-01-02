package com.kappstats.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js


class WasmRemoteApiImpl: RemoteApi {
    override val client: HttpClient = HttpClient(Js)
}
actual fun getRemoteApi(): com.kappstats.data.remote.api.RemoteApi = WasmRemoteApiImpl()