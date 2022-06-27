package com.ywq.plugin.aarmode

import org.gradle.api.Plugin
import org.gradle.api.Project

class ProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        println("#### project plugin")

        target.afterEvaluate {
            target.dependencies.apply {
                add("implementation", module("com.ywq.test:liba:1.0-SNAPSHOT"))
            }
        }
    }
}