import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}


dependencies {
    implementation(projects.composeApp)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)

    debugImplementation(libs.ui.tooling)
    testImplementation(libs.robolectric)
}

android {
    namespace = "com.kappstats"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
    defaultConfig {
        applicationId = "com.kappstats"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

tasks.register<Exec>("installAndRun") {
    group = "install"
    description = "Install debug and run."
    dependsOn("installDebug")
    val appId = android.defaultConfig.applicationId
    val mainActivity = ".MainActivity"
    commandLine("adb", "shell", "am", "start", "-n", "$appId/$appId$mainActivity")
}