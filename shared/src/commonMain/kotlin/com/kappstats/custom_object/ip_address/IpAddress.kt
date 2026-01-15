package com.kappstats.custom_object.ip_address

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline

@Serializable(with = IpAddress.IpAddressSerializer::class)
@JvmInline
value class IpAddress(val value: String) {

    init {
        require(isValidIpAddress(value)) {
            "IpAddress error: Invalid IP address format. Current value: $value"
        }
    }

    val asString: String
        get() = value

    companion object {
        private val IPV4_REGEX =
            """^((25[0-5]|2[0-4]\d|[01]?\d\d?)\.){3}(25[0-5]|2[0-4]\d|[01]?\d\d?)$""".toRegex()

        private fun Char.isHexDigit(): Boolean = this in '0'..'9' || this in 'a'..'f' || this in 'A'..'F'

        private fun isValidHextet(hextet: String): Boolean {
            if (hextet.length !in 1..4) return false
            return hextet.all { it.isHexDigit() }
        }

        private fun isIPv4(ip: String): Boolean {
            return IPV4_REGEX.matches(ip)
        }

        private fun isIPv6(ip: String): Boolean {
            if (ip.length < 2 || ip.length > 39) return false

            val doubleColonCount = ip.windowed(2).count { it == "::" }
            if (doubleColonCount > 1) return false

            var normalizedIp = ip
            if (doubleColonCount == 1) {
                if (ip == "::") {
                    normalizedIp = "0:0:0:0:0:0:0:0"
                } else {
                    val parts = ip.split("::", limit = 2)
                    val firstPart = parts[0]
                    val secondPart = parts[1]

                    fun countValidHextetsInSegment(segment: String): Int {
                        if (segment.isEmpty()) return 0
                        val hextets = segment.split(':')
                        if (hextets.any { !isValidHextet(it) }) return -1
                        return hextets.size
                    }

                    val hextetsInFirst = countValidHextetsInSegment(firstPart)
                    val hextetsInSecond = countValidHextetsInSegment(secondPart)

                    if (hextetsInFirst < 0 || hextetsInSecond < 0) return false
                    if (hextetsInFirst + hextetsInSecond >= 8) return false

                    val zerosToInsert = 8 - (hextetsInFirst + hextetsInSecond)
                    if (zerosToInsert < 1) return false

                    val zeroExpansion = List(zerosToInsert) { "0" }.joinToString(":")

                    normalizedIp = when {
                        firstPart.isEmpty() -> "$zeroExpansion:$secondPart"
                        secondPart.isEmpty() -> "$firstPart:$zeroExpansion"
                        else -> "$firstPart:$zeroExpansion:$secondPart"
                    }
                    if (normalizedIp.startsWith(":")) {
                        if (!firstPart.isEmpty()) return false
                    }
                    if (normalizedIp.endsWith(":")) {
                         if (!secondPart.isEmpty()) return false
                    }
                    if (normalizedIp.contains("::")) return false
                }
            }
            
            val hextets = normalizedIp.split(':')
            if (hextets.size != 8) return false
            return hextets.all { isValidHextet(it) }
        }

        fun isValidIpAddress(ip: String): Boolean {
            return isIPv4(ip) || isIPv6(ip)
        }
    }

    object IpAddressSerializer : KSerializer<IpAddress> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("IpAddress", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: IpAddress) {
            encoder.encodeString(value.value)
        }

        override fun deserialize(decoder: Decoder): IpAddress {
            return IpAddress(decoder.decodeString())
        }
    }
}
