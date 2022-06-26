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
        target.afterEvaluate {
            generateSubstitutes(target)
        }
    }

    private fun generatePublishScript(target: Project) {
        if (target.properties.containsKey(FLAG_GENERATE_PUBLISH).not()) {
            return
        }

        val genPublishWriter = FileWriter(target.rootDir.absolutePath + "/" + "publish.sh", false)

        target.subprojects {
            val parentName = PathHelper.getParentName(project)
            val parentPath = if (parentName.isNotEmpty()) ":$parentName" else ""
            genPublishWriter.write("./gradlew $parentPath:${project.name}:publishToMavenLocal -P${PublisherPlugin.USE_FLAG}\n")
        }

        genPublishWriter.close()
    }

    private fun generateSubstitutes(target: Project) {
        if (target.properties.containsKey(FLAG_GENERATE_SUBSTITUTE).not()) {
            return
        }

        val genSubstituteWriter =
            FileWriter(target.rootDir.absolutePath + "/" + substituteFileName, false)

        val substitutes = Substitutes()
        var projectCounter = 0

        target.subprojects {
            projectCounter++
            project.afterEvaluate {
                val parentName = PathHelper.getParentName(project)
                val parentPath = if (parentName.isNotEmpty()) ":$parentName" else ""
                val notation = "${project.group}:${project.name}:${project.version}"
                substitutes.configs.add(Config("$parentPath:${project.name}", notation, false))
                genSubstituteWriter.write(Gson().toJson(substitutes))
                projectCounter--
                if (projectCounter == 0) {
                    genSubstituteWriter.close()
                }
            }
        }
    }
}