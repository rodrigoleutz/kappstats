package com.kappstats.custom_object.username

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline

@Serializable(with = Username.UsernameSerializer::class)
@JvmInline
value class Username(private val value: String) {

    init {
        require(isValidUsername(value)) {
            "Username error: must contain at least one number. Current value: $value"
        }
    }

    val asString: String
        get() = value

    companion object {
        fun isValidUsername(username: String): Boolean {
            return username.any { it.isDigit() }
        }
    }

    object UsernameSerializer : KSerializer<Username> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Username", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Username) {
            encoder.encodeString(value.value)
        }

        override fun deserialize(decoder: Decoder): Username {
            return Username(decoder.decodeString())
        }
    }
}
