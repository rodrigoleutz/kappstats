package com.kappstats.domain.model.email

import kotlinx.serialization.Serializable


@Serializable
data class EmailResponse(
    val request: EmailRequest,
    val messageId: String?,
    val result: EmailResult
)
