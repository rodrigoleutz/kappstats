package com.kappstats.data.data_source.remote.api.email

import com.kappstats.domain.model.email.EmailRequest
import com.kappstats.domain.model.email.EmailResponse

interface EmailApi  {
    suspend fun send(emailRequest: EmailRequest): EmailResponse
}