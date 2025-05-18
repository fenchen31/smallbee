plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
val config = rootProject.extra.get("android") as Map<*, *>
val version = rootProject.extra.get("version") as Map<*, *>
java {
    sourceCompatibility = config.get("sourceCompatibility") as JavaVersion
    targetCompatibility = config.get("targetCompatibility") as JavaVersion
}
dependencies{
    annotationProcessor("com.google.auto.service:auto-service:${version.get("autoServiceCompileVersion")}")
}
