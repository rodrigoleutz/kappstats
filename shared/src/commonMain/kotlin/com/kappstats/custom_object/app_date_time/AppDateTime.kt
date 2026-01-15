package com.kappstats.custom_object.app_date_time

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


@Serializable(with = AppDateTime.AppDateTimeSerializer::class)
@JvmInline
value class AppDateTime(val value: String) : Comparable<AppDateTime> {

    init {
        require(isValidDateTime(value)) { "AppDateTime not valid: $value" }
    }

    object Pattern {
        const val DATE_DD_MM_YYYY = "dd/MM/yyyy"
        const val DATE_MM_DD_YYYY = "MM/dd/yyyy"
        const val DATE_YYYY_MM_DD = "yyyy/MM/dd"
        const val FULL_DATE_TIME = "MMMM d, yyyy 'at' h:mm a"
        const val FULL_DATE_TIME_SIMPLE = "dd/MM/yyyy hh:mm"
        const val FULL_DATE_TIME_SIMPLE_WITH_SECONDS = "dd/MM/yyyy hh:mm:ss"
        const val FULL_DATE_TIME_WITH_SECONDS = "MMMM d, yyyy 'at' h:mm:ss a"
        const val TIME_H_M_S_12HRS = "h:mm:ss a"
        const val TIME_H_M_12HRS = "h:mm a"
        const val TIME_H_M_S_24HRS = "hh:mm:ss"
        const val TIME_H_M_24HRS = "hh:mm"
    }

    companion object {

        @OptIn(ExperimentalTime::class)
        val now: AppDateTime
            get() = AppDateTime(Clock.System.now().toString())

        @OptIn(ExperimentalTime::class)
        fun isValidDateTime(value: String): Boolean {
            return try {
                Instant.parse(value)
                true
            } catch (e: Exception) {
                false
            }
        }

        @OptIn(ExperimentalTime::class)
        fun fromInstant(instant: Instant): AppDateTime {
            return AppDateTime(instant.toString())
        }

        @OptIn(ExperimentalTime::class)
        fun fromString(value: String): AppDateTime {
            try {
                val instant = Instant.parse(value)
                return AppDateTime(instant.toString())
            } catch (e: Exception) {
                throw IllegalArgumentException("AppDateTime not valid: $value")
            }
        }
    }

    object AppDateTimeSerializer : KSerializer<AppDateTime> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("AppDateTime", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: AppDateTime) {
            encoder.encodeString(value.value)
        }

        override fun deserialize(decoder: Decoder): AppDateTime {
            return AppDateTime(decoder.decodeString())
        }
    }

    @OptIn(ExperimentalTime::class)
    val day: String
        get() = toInstant().toLocalDateTime(TimeZone.UTC).day.toString()

    @OptIn(ExperimentalTime::class)
    val month: String
        get() = this.toInstant().toLocalDateTime(TimeZone.UTC).month.toString()

    @OptIn(ExperimentalTime::class)
    val year: String
        get() = this.toInstant().toLocalDateTime(TimeZone.UTC).year.toString()

    override fun toString(): String = value

    fun LocalDateTime.format(pattern: String): String {
        val monthsFull = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val monthsAbbrev = monthsFull.map { it.take(3) }
        val amPm = listOf("AM", "PM")
        val tokenRegex = Regex("yyyy|MMMM|MMM|MM|dd|d|HH|hh|h|mm|ss|a|'[^']*'")
        return tokenRegex.replace(pattern) { match ->
            when (val token = match.value) {
                "yyyy" -> year.toString().padStart(4, '0')
                "MMMM" -> monthsFull[month.number - 1]
                "MMM" -> monthsAbbrev[month.number - 1]
                "MM" -> month.number.toString().padStart(2, '0')
                "dd" -> day.toString().padStart(2, '0')
                "d" -> day.toString()
                "HH" -> hour.toString().padStart(2, '0')
                "hh" -> hour.toString().padStart(2, '0')
                "h" -> {
                    val h = hour % 12
                    (if (h == 0) 12 else h).toString()
                }
                "mm" -> minute.toString().padStart(2, '0')
                "ss" -> second.toString().padStart(2, '0')
                "a" -> amPm[if (hour < 12) 0 else 1]
                else -> if (token.startsWith("'")) token.drop(1).dropLast(1) else token
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun formatInstant(pattern: String, timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
        val localDateTime = this.toInstant().toLocalDateTime(timeZone)
        return localDateTime.format(pattern)
    }

    @OptIn(ExperimentalTime::class)
    fun toLong(): Long = try {
        this.toInstant().toEpochMilliseconds()
    } catch (e: Exception) {
        e.printStackTrace()
        -1
    }

    @OptIn(ExperimentalTime::class)
    fun toLongOrNull(): Long? = try {
        this.toInstant().toEpochMilliseconds()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    override operator fun compareTo(other: AppDateTime): Int = value.compareTo(other.value)

    @OptIn(ExperimentalTime::class)
    operator fun minus(appDateTimeUnit: AppDateTimeUnit): AppDateTime =
        fromInstant(toInstant().minus(appDateTimeUnit.value.milliseconds))

    @OptIn(ExperimentalTime::class)
    operator fun minus(appDateTime: AppDateTime): AppDateTimeUnit =
        AppDateTimeUnit(toInstant().minus(appDateTime.toInstant()).inWholeMilliseconds)

    @OptIn(ExperimentalTime::class)
    operator fun plus(appDateTimeUnit: AppDateTimeUnit): AppDateTime =
        fromInstant(toInstant().plus(appDateTimeUnit.value.milliseconds))

    @OptIn(ExperimentalTime::class)
    operator fun plus(mills: Long): AppDateTime =
        fromInstant(toInstant().plus(mills.milliseconds))

    @OptIn(ExperimentalTime::class)
    fun toInstant(): Instant = Instant.parse(this.value)

}