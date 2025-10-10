@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package com.jjasystems.chirp.convention

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

private val hierarchyTemplate = KotlinHierarchyTemplate {
    sourceSetTrees(
        KotlinSourceSetTree.main,
        KotlinSourceSetTree.test
    )

    common {
        withCompilations { true }

        group("jvmCommon") {
            withAndroidTarget()
            withJvm()
        }

        group("mobile") {
            withAndroidTarget()

            group("ios") {
                withIos()
            }

        }

        group("native") {
            withNative()

            group("apple") {
                withApple()

                group("ios") {
                    withIos()
                }

                group("macos") {
                    withMacos()
                }
            }
        }
    }
}

fun KotlinMultiplatformExtension.applyHierarchyTemplate() {
    applyHierarchyTemplate(hierarchyTemplate)
}