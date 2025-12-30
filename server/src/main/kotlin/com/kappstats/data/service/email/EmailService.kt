package com.kappstats.data.service.email

import com.kappstats.data.service.Service
import com.kappstats.domain.model.email.EmailRequest
import com.kappstats.domain.model.email.EmailResponse

interface EmailService: Service<EmailRequest, EmailResponse> {

}