package com.kappstats.domain.model.email

enum class EmailResult(
    val result: String
) {
    SendFailed(result = "sendFailed"),
    MessagingException(result = "messagingException"),
    Success(result = "success"),
    Unknown(result = "unknown");

    companion object {
        private val errorResults = entries
            .filter { it != Success && it != Unknown }
            .associateBy { it.result }

        fun fromResult(result: String?): EmailResult? = errorResults[result]
    }
}