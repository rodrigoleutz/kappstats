plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
}

group = "com.kappstats"
version = "1.0.0"
application {
    mainClass.set("com.kappstats.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {

    // Default -> Change engine to CIO
    implementation(projects.shared)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)

    implementation(projects.server.ksp)
    ksp(projects.server.ksp)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverCio)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.thymeleaf)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(platform(libs.mongodb.driver.bom))
    implementation(libs.mongodb.driver.kotlin.coroutine)
    implementation(libs.bson.kotlin)
    implementation(libs.bson.kotlinx)
    implementation(libs.jakarta.mail.api)
    implementation(libs.jakarta.mail)
    implementation(libs.commons.codec)

    // Test
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.insert.koin.koin.test)
    testImplementation(libs.koin.test.junit5)
    testImplementation(libs.testcontainers)
    testImplementation(libs.mongodb)
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

tasks.named("build") {
    dependsOn(":shared:kspKotlinMetadata")
}

ksp {
    arg("sourceSet", "main")
}

tasks.named<JavaExec>("run") {
    val envFile = file(".env")
    if (envFile.exists()) {
        val envVars = envFile.readLines()
            .mapNotNull { line ->
                val trimmed = line.trim()
                if (trimmed.isNotEmpty() && !trimmed.startsWith("#") && "=" in trimmed) {
                    val (key, value) = trimmed.split("=", limit = 2)
                    key.trim() to value.trim()
                } else null
            }.toMap()

        environment(envVars)
        println(".env loaded: (${envFile.absolutePath})")
    } else {
        println(".env not found: ${envFile.absolutePath}")
    }
}