package com.ywq.plugin.aarmode

import com.google.gson.Gson
import org.gradle.api.initialization.Settings
import java.io.FileReader

data class Substitutes(
    var open: Boolean = false,
    var configs: MutableList<Config> = mutableListOf()
) {

    companion object {
        const val FILE_NAME = "substitutes.json"

        private var substitutes: Substitutes? = null
        private var map: MutableMap<String, Config> = mutableMapOf()

        fun init(target: Settings) {
            try {
                val fileName = target.rootDir.absolutePath + "/" + FILE_NAME
                substitutes = Gson().fromJson(FileReader(fileName), Substitutes::class.java)
                substitutes?.configs?.forEach {
                    map[it.name!!] = it
                }
            } catch (e: Exception) {
                println("$FILE_NAME not exist.")
            }
        }

        fun getConfig(name: String): Config? {
            return map[name]
        }
    }
}

data class Config(
    var name: String? = null,
    var notation: String? = null,
    var substitute: Boolean = false
)