package com.ywq.plugin.aarmode

import org.gradle.api.Plugin
import org.gradle.api.initialization.ProjectDescriptor
import org.gradle.api.initialization.Settings

class SettingsPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        Substitutes.init(target)

        target.gradle.settingsEvaluated {
            println("#### settingsEvaluated")
            removeChildProject(this.rootProject.children)
        }
    }

    private fun removeChildProject(children: Set<ProjectDescriptor>) {
        val iterator = children.iterator() as MutableIterator

        while (iterator.hasNext()) {
            val pd = iterator.next()

            val config = Substitutes.getConfig(pd.name) ?: Substitutes.getConfig(":${pd.name}")

            if (config != null && config.substitute) {
                iterator.remove()
            } else if (pd.children.isNotEmpty()) {
                removeChildProject(pd.children)
            }
        }
    }
}