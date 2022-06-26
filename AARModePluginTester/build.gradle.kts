import com.ywq.plugin.aarmode.implementationX
import com.ywq.plugin.aarmode.projectX

plugins {
    kotlin("jvm") version "1.6.10"
    java
}

group = "com.ywq.test"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementationX(projectX(":liba"))
//    implementationX("com.ywq.test:liba:1.0-SNAPSHOT")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

apply(plugin = "AARModeGenerator")

allprojects {
    project.afterEvaluate {
        project.apply(plugin = "AARModePublisher")
    }
}
