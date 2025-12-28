package com.kappstats.custom_object.password

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline

@Serializable(with = Password.PasswordSerializer::class)
@JvmInline
value class Password(private val value: String) {

    init {
        require(isValidPassword(value)) {
            val errors = mutableListOf<String>()
            if (value.length < 8) errors.add("be at least 8 characters long")
            if (!value.any { it.isDigit() }) errors.add("contain at least one number")
            if (!value.any { it.isUpperCase() }) errors.add("contain at least one uppercase letter")
            if (value.none { it.isLetterOrDigit().not() }) errors.add("contain at least one special character")
            "Password error: must ${errors.joinToString(", ")}. Current value: $value"
        }
    }

    val asString: String
        get() = value

    companion object {
        fun isValidPassword(password: String): Boolean {
            if (password.length < 8) return false
            if (!password.any { it.isDigit() }) return false
            if (!password.any { it.isUpperCase() }) return false
            if (password.none { it.isLetterOrDigit().not() }) return false
            return true
        }
    }

    object PasswordSerializer : KSerializer<Password> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Password", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Password) {
            encoder.encodeString(value.value)
        }

        override fun deserialize(decoder: Decoder): Password {
            return Password(decoder.decodeString())
        }
    }
}
