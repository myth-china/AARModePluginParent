package com.ywq.plugin.aarmode

import org.gradle.api.Project
import java.io.FileReader
import java.util.*

object LocalProperties {

    private val properties = Properties()

    fun load(project: Project) {
        if (project.rootProject.file("local.properties").exists()) {
            properties.load(FileReader(project.rootProject.file("local.properties")))
        }
    }

    fun get(key: String): Any? {
        return properties[key]
    }
}