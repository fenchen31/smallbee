plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}
val config = rootProject.extra.get("android") as? Map<*, *>
val version = rootProject.extra.get("version") as? Map<*, *>
android {
    namespace = "com.practice.common"
    compileSdk = config?.get("compileSdk") as Int

    defaultConfig {
        minSdk = config.get("minSdk") as Int
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = config.get("isMinifyEnabled") as Boolean
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = config.get("sourceCompatibility") as JavaVersion
        targetCompatibility = config.get("targetCompatibility") as JavaVersion
    }
    kotlinOptions {
        jvmTarget = config.get("jvmTarget") as String
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}