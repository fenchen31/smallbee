plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
}
val config = rootProject.extra.get("android") as? Map<*, *>
val version = rootProject.extra.get("version") as? Map<*, *>
android {
    namespace = "com.practice.smallbee"
    compileSdk = config?.get("compileSdk") as Int

    defaultConfig {
        applicationId = "com.practice.smallbee"
        minSdk = config.get("minSdk") as Int
        targetSdk = config.get("targetSdk") as Int
        versionCode = config.get("versionCode") as Int
        versionName = config.get("versionName") as String
        javaCompileOptions {
            annotationProcessorOptions {
                //路由跳转配置
                arguments.putAll(mutableMapOf("MODULE_NAME" to "APP"))
            }
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    dataBinding{
        enable = config.get("dataBinding") as Boolean
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    kapt(project(":annotation_compiler"))
    implementation(project(":annotation_core"))
    implementation(project(":annotation"))
    api(project(":common"))

    //上拉加载下拉刷新
    implementation  (libs.refresh.layout.kernel)      //核心必须依赖
    implementation  (libs.scwang90.refresh.header.classics)    //经典刷新头
    implementation  (libs.refresh.footer.classics)    //经典加载
}