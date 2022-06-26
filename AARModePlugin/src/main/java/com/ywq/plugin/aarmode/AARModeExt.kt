package com.ywq.plugin.aarmode

import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.project

fun Project.hello() {
    println("Hello $name!")
}

fun DependencyHandler.implementationX(dependencyNotation: Any): Dependency? {
    println("########")
    println(dependencyNotation.toString() + "/" + dependencyNotation.javaClass)

    val newDN = if (dependencyNotation is String) {
        val config =
            Substitutes.getConfig(dependencyNotation) ?: Substitutes.getConfig(":$dependencyNotation")
        if (config?.substitute == true) {
            config.notation ?: dependencyNotation
        } else {
            dependencyNotation
        }
    } else {
        dependencyNotation
    }
    return this.add("implementation", newDN)
}

fun DependencyHandler.projectX(
    path: String,
    configuration: String? = null
): Any {
    val config =
        Substitutes.getConfig(path) ?: Substitutes.getConfig(path.substring(1))
    if (config == null || !config.substitute) {
        return project(path, configuration)
    }
    return path
}


fun Settings.hello() {
    println("Hello ${this.rootDir}!")
}

fun Settings.includeX(vararg projectPaths: String) {
    val nPP = projectPaths.filter {
        val config = Substitutes.getConfig(it) ?: Substitutes.getConfig(":$it")
        config == null || config.substitute.not()
    }

    this.include(*(nPP.toTypedArray()))
}