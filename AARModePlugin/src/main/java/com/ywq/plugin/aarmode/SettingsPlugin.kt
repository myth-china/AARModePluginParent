package com.ywq.plugin.aarmode

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class SettingsPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        Substitutes.init(target)
    }
}