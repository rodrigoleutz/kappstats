import com.android.build.api.dsl.androidLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget()

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()
    
    js {
        browser()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
        }
        commonMain.dependencies {
            // Default
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.ui)
            api(compose.components.resources)
            api(compose.components.uiToolingPreview)
            api(libs.androidx.lifecycle.viewmodelCompose)
            api(libs.androidx.lifecycle.runtimeCompose)
            api(projects.shared)

            // Serialization
            api(libs.kotlinx.serialization.json)

            // Logger
            api(libs.kermit)

            // Navigation
            api(libs.navigation.navigation3.ui)
            api(libs.navigation.material3.adaptiveNavigation3)
            api(libs.navigation.lifecycle.viewmodelNavigation3)

            // Icons
            api(libs.composeIcons.cssGg)
            api(libs.composeIcons.weatherIcons)
            api(libs.composeIcons.evaIcons)
            api(libs.composeIcons.feather)
            api(libs.composeIcons.fontAwesome)
            api(libs.composeIcons.lineAwesome)
            api(libs.composeIcons.linea)
            api(libs.composeIcons.octicons)
            api(libs.composeIcons.simpleIcons)
            api(libs.composeIcons.tablerIcons)

            // Settings
            api(libs.multiplatform.settings.no.arg)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
        webMain.dependencies {
            api(libs.navigation3.browser)
        }
    }
}

compose {
    resources {
        nameOfResClass = "ResComponents"
        publicResClass = true
        packageOfResClass = "com.kappstats.components.resources"
        generateResClass = always
    }
}
android {
    namespace = "com.kappstats.components"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
