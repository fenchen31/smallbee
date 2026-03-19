plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.kotlin.compose)
}
val config = rootProject.extra.get("android") as? Map<*, *>
val version = rootProject.extra.get("version") as? Map<*, *>
android {
    namespace = "com.practice.smallbee"
    compileSdk = config?.get("compileSdk") as Int

    defaultConfig {
        applicationId = "com.practice.smallbee"
        minSdk = config["minSdk"] as Int
        targetSdk = config["targetSdk"] as Int
        versionCode = config["versionCode"] as Int
        versionName = config["versionName"] as String
        javaCompileOptions {
            annotationProcessorOptions {
                //路由跳转配置
                arguments.putAll(mutableMapOf("MODULE_NAME" to "APP"))
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = config["dataBinding"] as Boolean
        compose = true
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
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    kapt(project(":annotation_compiler"))
    implementation(project(":annotation_core"))
    implementation(project(":annotation"))
    api(project(":common"))

    //上拉加载下拉刷新
    implementation  (libs.refresh.layout.kernel)      //核心必须依赖
    implementation  (libs.scwang90.refresh.header.classics)    //经典刷新头
    implementation  (libs.refresh.footer.classics)    //经典加载
}