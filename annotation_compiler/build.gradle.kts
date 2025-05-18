import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    kotlin("kapt")
}

val config = rootProject.extra.get("android") as Map<*, *>
val version = rootProject.extra.get("version") as Map<*, *>
java {
    sourceCompatibility = config.get("sourceCompatibility") as JavaVersion
    targetCompatibility = config.get("targetCompatibility") as JavaVersion
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.valueOf("JVM_" + config.get("jvmTarget") as String)
    }
}
dependencies {
    implementation("com.google.auto.service:auto-service:${version.get("autoServiceVersion") as String}")
    implementation(project(":annotation"))
    kapt(project(":annotation"))
}
