plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
}
val config = rootProject.extra.get("android") as? Map<*, *>
val version = rootProject.extra.get("version") as? Map<*, *>
android {
    namespace = "com.practice.common"
    compileSdk = config?.get("compileSdk") as Int

    defaultConfig {
        minSdk = config["minSdk"] as Int
        consumerProguardFiles("consumer-rules.pro")
        javaCompileOptions {
            annotationProcessorOptions {
                //路由跳转配置
                arguments.putAll(mutableMapOf("MODULE_NAME" to "COMMON"))
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = config["isMinifyEnabled"] as Boolean
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = config["sourceCompatibility"] as JavaVersion
        targetCompatibility = config["targetCompatibility"] as JavaVersion
    }
    dataBinding {
        enable = config["dataBinding"] as Boolean
    }
    kotlinOptions {
        jvmTarget = config["jvmTarget"] as String
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    kapt(project(":annotation_compiler"))
    implementation(project(":annotation_core"))
    implementation(project(":annotation"))
    api(libs.retrofit)
    api(libs.gson)
    api(libs.okhttp)
    api(libs.glide)
    api(libs.eventbus)
}