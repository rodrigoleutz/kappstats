package com.kappstats.domain.web_socket.action.authenticate.user

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.data.repository.user.ProfileRepository
import com.kappstats.domain.model.connection.AuthConnectionInfo
import com.kappstats.domain.model.connection.ConnectionInfo
import com.kappstats.domain.web_socket.contract.WebSocketContract
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse
import com.kappstats.dto.web_socket.WsAction
import com.kappstats.dto.web_socket.WsActionBase
import com.kappstats.model.user.Profile
import org.koin.core.component.inject

@WsAction
object AuthUserProfileUpdateAction : WebSocketContract<Profile, Profile?> {

    private val profileRepository by inject<ProfileRepository>()

    override val base: WsActionBase<Profile, Profile?> =
        WebSocketEvents.Authenticate.User.ProfileUpdate

    override suspend fun WebSocketRequest.process(
        connectionInfo: ConnectionInfo,
        data: Profile?
    ): WebSocketResponse? {
        if (connectionInfo !is AuthConnectionInfo) return null
        if (data == null || connectionInfo.profileId != data.id) return null
        val old = profileRepository.generic.getById(connectionInfo.profileId)
            ?: return this.toFailure(
                WebSocketResponse.Companion.FailureType.NoData,
                profileIdList = listOf(connectionInfo.profileId)
            )
        val updatedProfile = old.copy(
            name = data.name,
            username = data.username,
            bio = data.bio,
            updatedAt = old.updatedAt + listOf(AppDateTime.now)
        )
        val update = profileRepository.generic.update(updatedProfile)
            ?: return this.toFailure(
                WebSocketResponse.Companion.FailureType.SaveData,
                listOf(connectionInfo.profileId)
            )
        return this.toSuccess(update, listOf(connectionInfo.profileId))
    }
}