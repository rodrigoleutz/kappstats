package com.kappstats.data.service.email

import com.kappstats.constants.APP_NAME
import com.kappstats.domain.model.email.EmailRequest
import com.kappstats.domain.model.email.EmailResponse
import com.kappstats.domain.model.email.EmailResult
import jakarta.mail.Authenticator
import jakarta.mail.Message
import jakarta.mail.MessagingException
import jakarta.mail.PasswordAuthentication
import jakarta.mail.SendFailedException
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.Date
import java.util.Properties
import java.util.UUID

class EmailServiceImpl : EmailService {

    override suspend fun run(value: EmailRequest): EmailResponse {
        val result = send(
            smtp = value.smtp,
            username = value.username,
            token = value.token,
            senderName = value.senderName,
            senderEmail = value.senderEmail,
            receiverEmail = value.receiverEmail,
            receiverName = value.receiverName,
            subject = value.subject,
            linkUnsubscribe = value.linkUnsubscribe,
            body = value.body,
            isHtml = value.isHtml
        )
        val errorType = EmailResult.fromResult(result)
        if (result == null || errorType != null) {
            return EmailResponse(
                request = value,
                messageId = null,
                result = errorType ?: EmailResult.Unknown
            )
        }
        return EmailResponse(
            request = value,
            messageId = result,
            result = EmailResult.Success
        )
    }

    private fun send(
        smtp: String,
        username: String,
        token: String,
        senderName: String,
        senderEmail: String,
        receiverName: String,
        receiverEmail: String,
        subject: String,
        linkUnsubscribe: String?,
        body: String,
        isHtml: Boolean
    ): String? {
        val props = Properties().apply {
            put("mail.smtp.host", smtp)
            put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            put("mail.smtp.socketFactory.port", "465")
            put("mail.smtp.port", "465")
            put("mail.smtp.auth", "true")
            put("mail.smtp.connectiontimeout", "5000")
            put("mail.smtp.timeout", "5000")
            put("mail.smtp.writetimeout", "5000")
        }

        val session = Session.getDefaultInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication? {
                return PasswordAuthentication(username, token)
            }
        })

//        session.debug = true
//        System.setProperty("mail.debug", "true")
//        System.setProperty("mail.debug.verbose", "true")

        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(senderEmail, senderName))
            message.setRecipient(
                Message.RecipientType.TO,
                InternetAddress(receiverEmail, receiverName)
            )
            message.subject = subject
            message.sentDate = Date()

            if (isHtml) {
                message.setContent(body, "text/html; charset=utf-8")
            } else {
                message.setText(body)
            }

            val domain = senderEmail.substringAfter("@")
            val messageId = UUID.randomUUID()
            message.setHeader("X-Mailer", APP_NAME)
            message.setHeader("MIME-Version", "1.0")
            message.setHeader("Content-Transfer-Encoding", "7bit")
            message.setHeader("Return-Path", senderEmail)

            message.setHeader("Message-ID", "<$messageId@$domain>");
            if (linkUnsubscribe != null) {
                message.setHeader("List-Unsubscribe", linkUnsubscribe);
                message.setHeader("List-Unsubscribe-Post", "List-Unsubscribe=One-Click");
            }

            Transport.send(message)
            return message.messageID
        } catch (e: SendFailedException) {
            return EmailResult.SendFailed.result
        } catch (e: MessagingException) {
            System.err.println("Error sending email: ${e.message}")
            e.printStackTrace()
            return EmailResult.SendFailed.result
        } catch (e: Exception) {
            System.err.println("Error sending email: ${e.message}")
            e.printStackTrace()
            return null
        }
    }
}