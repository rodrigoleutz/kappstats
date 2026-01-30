package com.kappstats.util

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
object IdGenerator {

    val createUuid: String
        get() = Uuid.random().toHexString()

    val generateHashingId: String
        get() = Uuid.generateV7().toHexDashString()
}