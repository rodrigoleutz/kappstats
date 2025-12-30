package com.kappstats.domain.model.email

import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(
    val smtp: String,
    val username: String,
    val token: String,
    val senderName: String,
    val senderEmail: String,
    val receiverName: String,
    val receiverEmail: String,
    val subject: String,
    val linkUnsubscribe: String?,
    val body: String,
    val isHtml: Boolean = false
)
