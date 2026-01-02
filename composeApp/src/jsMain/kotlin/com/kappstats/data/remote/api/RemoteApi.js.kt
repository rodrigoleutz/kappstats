package com.kappstats.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js

class JsRemoteApiImpl: RemoteApi {
    override val client: HttpClient = HttpClient(Js)
}

actual fun getRemoteApi(): RemoteApi = JsRemoteApiImpl()