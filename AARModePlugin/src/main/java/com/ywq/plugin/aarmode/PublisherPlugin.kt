package com.ywq.plugin.aarmode

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get

class PublisherPlugin : Plugin<Project> {

    companion object {
        const val USE_FLAG = "publisher"
    }

    override fun apply(target: Project) {
        target.afterEvaluate {
            configMavenPublish(project)
        }
    }

    private fun configMavenPublish(project: Project) {
        project.run {
            if (project.hasProperty(USE_FLAG).not()) {
                return
            }

            if (!project.plugins.hasPlugin("com.android.library")) {
                return
            }

            if (plugins.hasPlugin("maven-publish")) {
                return
            }

            apply(plugin = "maven-publish")

            val isAndroidProject = hasProperty("android")

            if (isAndroidProject) {
                if (tasks.findByName("sourceJar") == null) {
                    tasks.register("sourceJar", Jar::class.java) {
                        val android = (project.extensions["android"] as LibraryExtension)
                        val srcDir = android.sourceSets["main"].java.srcDirs.toMutableSet()
                        android.productFlavors.all {
                            srcDir += android.sourceSets[this.name].java.srcDirs
                        }
                        from(srcDir)
                        archiveClassifier.set("sources")
                    }
                }
            } else {
                val java = project.extensions["java"] as JavaPluginExtension
                try {
                    java.withSourcesJar()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            extensions.configure("publishing", Action<PublishingExtension> {
                publications {
                    create<MavenPublication>("release") {
                        if (isAndroidProject) {
                            val android = (project.extensions["android"] as LibraryExtension)
                            val comp = if (android.productFlavors.isNotEmpty())
                                components.findByName("all")
                            else components.findByName("release")
                            from(comp)
                        } else {
                            from(components.getByName("java"))
                        }

                        this.groupId = project.group as String
                        this.version = project.version as String?
                    }
                }
            })
        }
    }
}