import com.jjasystems.chirp.convention.applyHierarchyTemplate
import com.jjasystems.chirp.convention.configureAndroidTarget
import com.jjasystems.chirp.convention.configureDesktopTarget
import com.jjasystems.chirp.convention.configureIosTarget
import com.jjasystems.chirp.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class CmpApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.jjasystems.convention.android.application.compose")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            configureAndroidTarget()
            configureIosTarget()
            configureDesktopTarget()

            extensions.configure<KotlinMultiplatformExtension> {
                applyHierarchyTemplate()
            }

            dependencies {
                "debugImplementation"(libs.findLibrary(("androidx-compose-ui-tooling")).get())
            }
        }
    }
}