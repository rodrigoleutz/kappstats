package com.kappstats.data.remote.data_source

import com.kappstats.constants.config.ProjectConfig
import com.kappstats.data.remote.api.RemoteApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class RemoteDataSourceImpl(
    private val remoteApi: RemoteApi
): RemoteDataSource {
    private val client: HttpClient = remoteApi.client.config {
        install(WebSockets) {
            pingIntervalMillis = 30_000
        }
        install(Logging) {
            level = if(ProjectConfig.GIT_BRANCH != "main") LogLevel.ALL else LogLevel.NONE
        }
        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    prettyPrint = true
                }
            )
        }
    }
}