package com.kappstats.data.remote.container

import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

object MongoTestContainer {
    val instance: MongoDBContainer by lazy {
        MongoDBContainer(DockerImageName.parse("mongo")).apply {
            start()
        }
    }
    val connectionString: String
        get() = instance.replicaSetUrl
}