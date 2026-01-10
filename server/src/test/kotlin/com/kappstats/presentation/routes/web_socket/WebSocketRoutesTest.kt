package com.kappstats.presentation.routes.web_socket

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.custom_object.app_date_time.seconds
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse
import com.kappstats.endpoint.AppEndpoints
import com.kappstats.test_util.BaseIntegrationTest
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class WebSocketRoutesTest: BaseIntegrationTest() {

    companion object {
        val webSocketRequest = WebSocketRequest(
            action = WebSocketEvents.Status.Ping.action,
            data = Json.encodeToString(AppDateTime.now)
        )
        val json = Json.encodeToString(webSocketRequest)
    }

    @Test
    fun `Test webSocket connection test ping`() = baseTestApplication { client ->
        client.webSocket(AppEndpoints.WebSocket.route) {
            send(json)
            val frame = incoming.receive() as Frame.Text
            val jsonDecoded =
                Json.decodeFromString<WebSocketResponse>(frame.readText())
            assert(jsonDecoded.isSuccess)
            assertInstanceOf(WebSocketResponse::class.java, jsonDecoded)
            val date = Json.decodeFromString<AppDateTime>((jsonDecoded as WebSocketResponse.Success).data)
            assertInstanceOf(AppDateTime::class.java, date)
            assert(date > (AppDateTime.now-10.seconds))
        }
    }
}