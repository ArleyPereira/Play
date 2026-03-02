import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidLibrary {
        namespace = "br.com.hellodev.design"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
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
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "design"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }

        commonMain.dependencies {
            // Domain
            implementation(project(":domain"))

            // Core
            implementation(project(":core"))

            // Swipeable
            implementation(libs.swipeable.kmp)

            // Material 3
            implementation(libs.material3)

            // Compose Core
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)

            // Compose Resources
            implementation(libs.compose.components.resources)

            // Compose UI Tooling Preview
            implementation(libs.compose.ui.tooling.preview)
        }

        commonTest.dependencies {

        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.androidx.ui.tooling)
}
