package com.ywq.plugin.aarmode

import com.google.gson.Gson
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.FileWriter

class GeneratorPlugin : Plugin<Project> {
    companion object {
        const val FLAG_GENERATE_PUBLISH = "g-publish"
        const val FLAG_GENERATE_SUBSTITUTE = "g-substitute"
    }

    private val substituteFileName = Substitutes.FILE_NAME

    override fun apply(target: Project) {
        generatePublishScript(target)
        generateSubstitutes(target)
    }

    private fun generatePublishScript(target: Project) {
        if (target.properties.containsKey(FLAG_GENERATE_PUBLISH).not()) {
            return
        }

        val genPublishWriter = FileWriter(target.rootDir.absolutePath + "/" + "publish.sh", false)

        target.subprojects {
            project.afterEvaluate {
                if (!project.plugins.hasPlugin("com.android.library")) {
                    return@afterEvaluate
                }

                val parentName = PathHelper.getParentName(project)
                val parentPath = if (parentName.isNotEmpty()) ":$parentName" else ""
                genPublishWriter.write("./gradlew $parentPath:${project.name}:publishToMavenLocal -P${PublisherPlugin.USE_FLAG}\n")
            }
        }

        target.gradle.projectsEvaluated {
            genPublishWriter.close()
        }
    }

    private fun generateSubstitutes(target: Project) {
        if (target.properties.containsKey(FLAG_GENERATE_SUBSTITUTE).not()) {
            return
        }

        if (target.plugins.isEmpty()) {
            return
        }

        val genSubstituteWriter =
            FileWriter(target.rootDir.absolutePath + "/" + substituteFileName, false)

        val substitutes = Substitutes()

        target.subprojects {
            project.afterEvaluate {

                if (!project.plugins.hasPlugin("com.android.library")) {
                    return@afterEvaluate
                }

                val parentName = PathHelper.getParentName(project)
                val parentPath = if (parentName.isNotEmpty()) ":$parentName" else ""
                val notation = "${project.group}:${project.name}:${project.version}"
                substitutes.configs.add(Config("$parentPath:${project.name}", notation, false))
            }
        }

        target.gradle.projectsEvaluated {
            genSubstituteWriter.write(Gson().toJson(substitutes))
            genSubstituteWriter.close()
        }
    }
}