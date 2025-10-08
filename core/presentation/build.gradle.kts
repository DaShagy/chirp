plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {
    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                // Add KMP dependencies here

                implementation(projects.core.domain)

                implementation(compose.components.resources)
                implementation(libs.material3.adaptive)

                implementation(libs.bundles.koin.common)
            }
        }

        val mobileMain by creating {
            dependencies {
                implementation(libs.moko.permissions)
                implementation(libs.moko.permissions.compose)
                implementation(libs.moko.permissions.notifications)
            }

            dependsOn(commonMain.get())
        }

        androidMain.get().dependsOn(mobileMain)

        val iosMain by creating {
            dependsOn(mobileMain)
        }

        listOf(
            iosArm64(),
            iosSimulatorArm64(),
            iosX64()
        ).forEach { target ->
            getByName("${target.name}Main") {
                dependsOn(iosMain)
            }
        }
    }

}