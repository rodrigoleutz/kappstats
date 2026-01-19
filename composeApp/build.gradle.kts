import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.mokkery)
}

kotlin {
    androidLibrary {
        namespace = "com.kappstats.library"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        androidResources {
            enable = true
        }
    }

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
        /* Main Release
        browser {
            commonWebpackConfig {
                sourceMaps = false
                cssSupport {
                    enabled.set(true)
                }
            }
            binaries.executable()
        }
         */
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        /* Main Release
        browser {
            commonWebpackConfig {
                sourceMaps = false
                cssSupport {
                    enabled.set(true)
                }
            }
            binaries.executable()
        }
         */
        binaries.executable()
    }

    sourceSets {
        androidMain.dependencies {
            // KStore
            implementation(libs.kstore.file)
        }
        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")

            dependencies {
                implementation(projects.composeComponents)

                // Koin
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.compose.viewmodel.navigation)

                // KStore
                implementation(libs.kstore)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.compose.ui.test)
            implementation(libs.mokkery)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            // AppDirs
            implementation(libs.appdirs)
            // KStore
            implementation(libs.kstore.file)
        }
        iosMain.dependencies {
            // KStore
            implementation(libs.kstore.file)
        }
        jsMain.dependencies {
            // KStore
            implementation(libs.kstore.storage)
        }
        wasmJsMain.dependencies {
            // KStore
            implementation(libs.kstore.storage)
        }
    }
}

compose {
    resources {
        nameOfResClass = "Res"
        publicResClass = false
        packageOfResClass = "com.kappstats.resources"
        generateResClass = ResourcesExtension.ResourceClassGeneration.Always
    }
}

compose.desktop {
    application {
        mainClass = "com.kappstats.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.AppImage)
            packageName = "com.kappstats"
            packageVersion = "1.0.0"
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", project(":composeApp:ksp"))
}

ksp {
    arg("ksp.incremental", "false")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
