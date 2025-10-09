package com.jjasystems.chirp.convention

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureKotlinMultiplatform() {

    extensions.configure<LibraryExtension> {
        namespace = this@configureKotlinMultiplatform.pathToPackageName()
    }

    configureAndroidTarget()
    configureDesktopTarget()

    extensions.configure<KotlinMultiplatformExtension> {
        listOf(
            iosArm64(),
            iosSimulatorArm64(),
            iosX64()
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = this@configureKotlinMultiplatform.pathToFrameworkName()
            }
        }

        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
        }
    }
}