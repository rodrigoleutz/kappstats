package com.kappstats.domain.web_socket.action.authenticate.user

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.data.repository.user.AuthRepository
import com.kappstats.domain.core.security.hashing.HashingService
import com.kappstats.domain.model.connection.AuthConnectionInfo
import com.kappstats.domain.model.connection.ConnectionInfo
import com.kappstats.domain.web_socket.contract.WebSocketContract
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse
import com.kappstats.dto.web_socket.WsAction
import com.kappstats.dto.web_socket.WsActionBase
import com.kappstats.model.user.Auth
import org.koin.core.component.inject

@WsAction
object AuthUserAuthUpdateAction : WebSocketContract<Pair<Pair<Email, Password?>, Password>, Auth?> {

    private val authRepository by inject<AuthRepository>()
    private val hashingService by inject<HashingService>()

    override val base: WsActionBase<Pair<Pair<Email, Password?>, Password>, Auth?> =
        WebSocketEvents.Authenticate.User.AuthUpdate

    override suspend fun WebSocketRequest.process(
        connectionInfo: ConnectionInfo,
        data: Pair<Pair<Email, Password?>, Password>?
    ): WebSocketResponse? {
        if (data == null) return this.toFailure(WebSocketResponse.Companion.FailureType.NoData)
        if (connectionInfo !is AuthConnectionInfo)
            return this.toFailure(WebSocketResponse.Companion.FailureType.Unauthorized)
        val authAndSaltedHash =
            authRepository.getAuthAndSaltedHashById(connectionInfo.authId)
                ?: return this.toFailure(WebSocketResponse.Companion.FailureType.LoadData)
        if (!hashingService.verifyHash(data.second.asString, authAndSaltedHash.second))
            return this.toFailure(WebSocketResponse.Companion.FailureType.Unauthorized)
        val updatedAuth = authAndSaltedHash.first.copy(
            email = data.first.first,
            updatedAt = authAndSaltedHash.first.updatedAt + listOf(AppDateTime.now)
        )
        val saltedHash =
            if (data.first.second != null)
                hashingService.generateSaltedHash(data.first.second!!.asString) else null
        val update = authRepository.update(updatedAuth, saltedHash)
            ?: return this.toFailure(WebSocketResponse.Companion.FailureType.SaveData)
        return this.toSuccess(
            data = update,
            profileIdList = listOf(connectionInfo.profileId)
        )
    }
}