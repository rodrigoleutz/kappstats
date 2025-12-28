package com.kappstats.custom_object.app_date_time

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import kotlin.jvm.JvmInline

@JvmInline
value class AppDateTimeUnit(val value: Long) {

    operator fun div(appDateTimeUnit: AppDateTimeUnit): AppDateTimeUnit =
        AppDateTimeUnit(value / appDateTimeUnit.value)

    operator fun div(long: Long): AppDateTimeUnit = AppDateTimeUnit(value / long)

    operator fun minus(appDateTimeUnit: AppDateTimeUnit): AppDateTimeUnit =
        AppDateTimeUnit(value - appDateTimeUnit.value)

    operator fun plus(appDateTimeUnit: AppDateTimeUnit): AppDateTimeUnit =
        AppDateTimeUnit(value + appDateTimeUnit.value)

    operator fun times(long: Long): AppDateTimeUnit = AppDateTimeUnit(value * long)

    operator fun compareTo(other: AppDateTimeUnit): Int = value.compareTo(other.value)

    companion object {
        val roundingMode = DecimalMode(
            decimalPrecision = 5,
            roundingMode = RoundingMode.ROUND_HALF_TO_EVEN
        )
    }

    val inMilliseconds: Long
        get() = value

    val inSeconds: Double
        get() {
            val seconds = BigDecimal.fromLong(value)
                .divide(BigDecimal.fromLong(1_000L), roundingMode)
            return seconds.doubleValue(false)
        }

    val inMinutes: Double
        get() {
            val minutes = BigDecimal.fromLong(value)
                .divide(BigDecimal.fromLong(1.minutes.value), roundingMode)
            return minutes.doubleValue(false)
        }

    val inHours: Double
        get() {
            val hours = BigDecimal.fromLong(value)
                .divide(BigDecimal.fromLong(1.hours.value), roundingMode)
            return hours.doubleValue(false)
        }

    val inDays: Double
        get() {
            val days = BigDecimal.fromLong(value)
                .divide(BigDecimal.fromLong(1.days.value), roundingMode)
            return days.doubleValue(false)
        }

    /**
     * inMonths: 30 days is equal 1 month
     */
    val inMonths: Double
        get() {
            val months = BigDecimal.fromLong(value)
                .divide(BigDecimal.fromLong(1.months.value), roundingMode)
            return months.doubleValue(false)
        }

    val inYears: Double
        get() {
            val years = BigDecimal.fromLong(value)
                .divide(BigDecimal.fromLong(1.years.value), roundingMode)
            return years.doubleValue(false)
        }

    override fun toString(): String = "$value ms"
}

val Long.milliseconds: AppDateTimeUnit
    get() = AppDateTimeUnit(this * 1L)

val Long.seconds: AppDateTimeUnit
    get() = AppDateTimeUnit(this * 1000L)

val Long.minutes: AppDateTimeUnit
    get() = this.seconds * 60L

val Long.hours: AppDateTimeUnit
    get() = this.minutes * 60L

val Long.days: AppDateTimeUnit
    get() = this.hours * 24L

val Long.months: AppDateTimeUnit
    get() = this.days * 30L

val Long.years: AppDateTimeUnit
    get() = this.days * 365L

val Int.milliseconds: AppDateTimeUnit
    get() = AppDateTimeUnit(this * 1L)

val Int.seconds: AppDateTimeUnit
    get() = AppDateTimeUnit(this * 1000L)

val Int.minutes: AppDateTimeUnit
    get() = this.seconds * 60L

val Int.hours: AppDateTimeUnit
    get() = this.minutes * 60L

val Int.days: AppDateTimeUnit
    get() = this.hours * 24L

val Int.months: AppDateTimeUnit
    get() = this.days * 30L

val Int.years: AppDateTimeUnit
    get() = this.days * 365L

val Double.milliseconds: AppDateTimeUnit
    get() = AppDateTimeUnit((this * 1L).toLong())

val Double.seconds: AppDateTimeUnit
    get() = AppDateTimeUnit((this * 1_000L).toLong())

val Double.minutes: AppDateTimeUnit
    get() = AppDateTimeUnit((this * 1_000L * 60L).toLong())

val Double.hours: AppDateTimeUnit
    get() = AppDateTimeUnit((this * 1_000L * 60L * 60L).toLong())

val Double.days: AppDateTimeUnit
    get() = AppDateTimeUnit((this * 1_000L * 60L * 60L * 24).toLong())

val Double.months: AppDateTimeUnit
    get() = AppDateTimeUnit((this * 1_000L * 60L * 60L * 24L * 30L).toLong())

val Double.years: AppDateTimeUnit
    get() = AppDateTimeUnit((this * 1_000L * 60L * 60L * 24L * 365L).toLong())

