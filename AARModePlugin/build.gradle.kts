plugins {
    kotlin("jvm") version "1.6.21"
    java
    `java-gradle-plugin`
    `maven-publish`
    `kotlin-dsl`
}

group = "com.ywq.plugin"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // https://mvnrepository.com/artifact/com.android.tools.build/gradle
    compileOnly("com.android.tools.build:gradle:4.2.2")
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.8.9")

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("AARModeGenerator") {
            id = "AARModeGenerator"
            displayName = "AARModeGeneratePlugin"
            description = "Generate publish script and substitutes.json"
            implementationClass = "com.ywq.plugin.aarmode.GeneratorPlugin"
        }

        create("AARModePublisher") {
            id = "AARModePublisher"
            displayName = "AARModePublisher"
            description = "Maven publish helper."
            implementationClass = "com.ywq.plugin.aarmode.PublisherPlugin"
        }

        create("AARModeSettings") {
            id = "AARModeSettings"
            displayName = "AARModeSettings"
            description = "Aar mode settings."
            implementationClass = "com.ywq.plugin.aarmode.SettingsPlugin"
        }
    }
}