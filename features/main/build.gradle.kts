import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidLibrary {
        namespace = "br.com.hellodev.main"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
        androidResources {
            enable = true
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.moko.permissions.gallery)
        }

        commonMain.dependencies {
            // Design
            implementation(project(":design"))

            // Core
            implementation(project(":core"))

            // Domain
            implementation(project(":domain"))

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Media Player
            implementation(libs.compose.multiplatform.media.player)

            // FileKit
            implementation(libs.filekit.dialogs.compose)

            // Swipeable
            implementation(libs.swipeable.kmp)

            // Compose Resources
            implementation(libs.compose.components.resources)
            implementation(libs.compose.navigation)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.material3)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }

        iosMain.dependencies {
            implementation(libs.moko.permissions.gallery)
        }
    }

}

dependencies {
    androidRuntimeClasspath(libs.androidx.ui.tooling)
}
