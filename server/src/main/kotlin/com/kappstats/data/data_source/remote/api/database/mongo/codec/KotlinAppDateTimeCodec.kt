package com.kappstats.data.data_source.remote.api.database.mongo.codec

import com.kappstats.custom_object.app_date_time.AppDateTime
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class KotlinAppDateTimeCodec : Codec<AppDateTime?> {

    @OptIn(ExperimentalTime::class)
    override fun encode(
        writer: BsonWriter?,
        value: AppDateTime?,
        encoderContext: EncoderContext,
    ) {
        if (value == null) {
            writer?.writeNull()
        } else {
            writer?.writeDateTime(value.toInstant().toEpochMilliseconds())
        }
    }

    override fun getEncoderClass(): Class<AppDateTime?> =
        AppDateTime::class.java as Class<AppDateTime?>

    @OptIn(ExperimentalTime::class)
    override fun decode(
        reader: BsonReader?,
        decoderContext: DecoderContext,
    ): AppDateTime? {
        if (reader?.currentBsonType == BsonType.NULL) {
            reader.readNull()
            return null
        }
        return AppDateTime(Instant.fromEpochMilliseconds(reader!!.readDateTime()).toString())
    }
}
