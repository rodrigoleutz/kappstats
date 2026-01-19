import com.google.devtools.ksp.gradle.KspAATask
import com.google.devtools.ksp.gradle.KspExtension
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    androidLibrary {
        namespace = "com.kappstats.shared"
        minSdk = libs.versions.android.minSdk.get().toInt()
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    iosArm64()
    iosSimulatorArm64()

    jvm()

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                // put your Multiplatform dependencies here
                api(libs.kotlinx.serialization.json)
                api(libs.bignum)
                api(libs.kotlinx.datetime)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }

}

dependencies {
    add("kspCommonMainMetadata", project(":shared:ksp"))
}

ksp {
    arg("ksp.incremental", "false")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

fun Project.getGitBranch(): String {
    return try {
        val branch = providers.exec {
            commandLine("git", "rev-parse", "--abbrev-ref", "HEAD")
        }.standardOutput.asText.get().trim()

        if (branch != "HEAD" && branch.isNotEmpty()) {
            branch
        } else {
            val env = System.getenv()
            env["CI_COMMIT_REF_NAME"]
                ?: env["CI_COMMIT_TAG"]
                ?: env["CI_BUILD_REF_NAME"]
                ?: "detached"
        }
    } catch (e: Exception) {
        System.getenv()["CI_COMMIT_REF_NAME"] ?: "unknown"
    }
}

val currentBranch = getGitBranch()

println("current branch: $currentBranch")
extensions.configure<KspExtension> {
    arg("GIT_BRANCH", currentBranch)
    arg("PROJECT_DIR", projectDir.absolutePath)
}

tasks.withType<KspAATask>().configureEach {
    inputs.file(layout.projectDirectory.file(".env"))
}