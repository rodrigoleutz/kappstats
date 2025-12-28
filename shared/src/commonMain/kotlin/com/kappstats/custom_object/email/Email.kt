package com.kappstats.custom_object.email

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline

@Serializable(with = Email.EmailSerializer::class)
@JvmInline
value class Email(val value: String) {
    init {
        require(isValidEmail(value)) { "Email error: $value is not a valid email." }
    }

    val asString: String
        get() = value

    companion object {
        fun isValidEmail(email: String): Boolean {
            if (email.isBlank()) {
                return false
            }

            val parts = email.split('@')
            if (parts.size != 2) {
                return false
            }

            val localPart = parts[0]
            val domainPart = parts[1]

            if (localPart.isEmpty() || domainPart.isEmpty()) {
                return false
            }

            if (localPart.startsWith(".") || localPart.endsWith(".")) {
                return false
            }
            if (localPart.contains("..")) {
                return false
            }
            val allowedLocalPartChars = ('a'..'z').toSet() +
                                      ('A'..'Z').toSet() +
                                      ('0'..'9').toSet() +
                                      setOf('!', '#', '$', '%', '&', '\'', '*', '+', '-', '/', '=', '?', '^', '_', '`', '{', '|', '}', '~', '.')
            if (!localPart.all { it in allowedLocalPartChars }) {
                return false
            }

            if (!domainPart.contains(".")) {
                return false
            }
            if (domainPart.startsWith(".") || domainPart.endsWith(".")) {
                return false
            }
            if (domainPart.startsWith("-") || domainPart.endsWith("-")) {
                return false
            }
            if (domainPart.contains("..") || domainPart.contains("--")) {
                return false
            }

            val domainLabels = domainPart.split('.')
            if (domainLabels.any { it.isEmpty() }) {
                return false
            }

            for (label in domainLabels) {
                if (label.startsWith("-") || label.endsWith("-")) {
                    return false
                }
                if (!label.all { it.isLetterOrDigit() || it == '-' }) {
                    return false
                }
            }

            val tld = domainLabels.last()
            if (tld.length < 2 || tld.length > 63 || !tld.all { it.isLetter() }) {
                return false
            }

            return true
        }
    }

    object EmailSerializer : KSerializer<Email> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Email", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Email) {
            encoder.encodeString(value.value)
        }

        override fun deserialize(decoder: Decoder): Email {
            return Email(decoder.decodeString())
        }
    }
}
