package com.ywq.plugin.aarmode

import org.gradle.api.Project

object PathHelper {

    fun getParentName(project: Project): String {
        return if (project.rootProject.parent == null) {
            ""
        } else {
            project.rootProject.name
        }
    }
}