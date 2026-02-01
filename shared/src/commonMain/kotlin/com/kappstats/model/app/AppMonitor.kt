package com.kappstats.model.app

import com.kappstats.contracts.Model
import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.util.IdGenerator
import kotlinx.serialization.Serializable

@Serializable
data class AppMonitor(
    override val id: String,
    val owner: String,
    val name: String,
    val description: String,
    val hashId: String = IdGenerator.generateHashingId,
    val members: Map<String, AppMemberType> = emptyMap(),
    override val createdAt: AppDateTime = AppDateTime.now,
    override val updatedAt: List<AppDateTime> = emptyList()
) : Model {

    val profileIdList: List<String>
        get() = listOf(owner) + members.keys
}
