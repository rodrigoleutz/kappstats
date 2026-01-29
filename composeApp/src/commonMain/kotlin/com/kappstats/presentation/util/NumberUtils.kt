package com.kappstats.presentation.util

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.RoundingMode

fun Long.fromMbToGbString(): String {
    val result =
        BigDecimal.fromLong(this)
            .divide(BigDecimal.fromInt(1024))
            .roundToDigitPositionAfterDecimalPoint(2, RoundingMode.ROUND_HALF_TO_EVEN)
            .toPlainString()
    return "${result}GB"
}

fun Long.fromBytesToGigabyteString(): String {
    val result =
        BigDecimal.fromLong(this)
            .divide(BigDecimal.fromInt(1024)) //K
            .divide(BigDecimal.fromInt(1024)) //M
            .divide(BigDecimal.fromInt(1024)) //G
            .roundToDigitPositionAfterDecimalPoint(2, RoundingMode.ROUND_HALF_TO_EVEN)
            .toPlainString()
    return "${result}GB"
}

fun Long.secondsToMinString(): String {
    return (this/60).toString()+" min"
}