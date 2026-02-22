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
        namespace = "br.com.hellodev.onboarding"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
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

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.moko.permissions.gallery)
        }

        commonMain.dependencies {
            // Design
            implementation(project(":design"))

            // Features - Main
            implementation(project(":features:main"))

            // Core
            //implementation(project(":core"))

            // Domain
            //implementation(project(":domain"))

            // Koin
            //implementation(libs.koin.core)
            //implementation(libs.koin.compose)
            //implementation(libs.koin.compose.viewmodel)

            // Moko Permissions
            implementation(libs.moko.permissions)
            implementation(libs.moko.permissions.compose)
            implementation(libs.moko.permissions.gallery)
            implementation(libs.compose.multiplatform.media.player)
            implementation(libs.filekit.core)

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
