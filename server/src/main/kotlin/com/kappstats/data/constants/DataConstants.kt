package com.kappstats.data.constants

object DataConstants {

    val databaseUrl = System.getenv()["DATABASE_URL"] ?: "172.25.0.5:27017"
    val databaseUser = System.getenv()["MONGODB_USER"] ?: "username"
    val databasePasswd = System.getenv()["MONGODB_PASSWD"] ?: "userpassword"
    val databaseString = "mongodb://$databaseUser:$databasePasswd@$databaseUrl"
    val databaseName = "KAppStats"
}