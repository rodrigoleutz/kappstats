package com.kappstats.data.data_source.remote.api.database.mongo

import com.kappstats.data.data_source.remote.api.database.mongo.codec.KotlinAppDateTimeCodec
import com.mongodb.ConnectionString
import com.mongodb.KotlinCodecProvider
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.kotlin.DataClassCodecProvider
import org.bson.codecs.pojo.PojoCodecProvider

class MongoApi(
    private val serverUrl: String,
    private val databaseName: String,
) {
    private val settings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(serverUrl))
        .codecRegistry(
            CodecRegistries.fromRegistries(
                CodecRegistries.fromCodecs(
                    KotlinAppDateTimeCodec(),
                ),
                CodecRegistries.fromProviders(
                    DataClassCodecProvider(),
                    PojoCodecProvider.builder().automatic(false).build(),
                    KotlinCodecProvider()
                ),
                MongoClientSettings.getDefaultCodecRegistry()
            )
        ).build()

    val client = MongoClient.create(settings)
    val database = client.getDatabase(databaseName)
}