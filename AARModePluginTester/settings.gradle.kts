import com.ywq.plugin.aarmode.includeX

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        classpath("com.ywq.plugin:AARModePlugin:1.0-SNAPSHOT")
    }
}

apply(plugin = "AARModeSettings")

rootProject.name = "AARModePluginTester"

includeX("liba")