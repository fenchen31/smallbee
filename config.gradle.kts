extra.apply{
    set("android", mapOf(
        "compileSdk" to 35,
        "minSdk" to 26,
        "targetSdk" to 35,
        "versionCode" to 1,
        "versionName" to "1.0",
        "sourceCompatibility" to JavaVersion.VERSION_11,
        "targetCompatibility" to JavaVersion.VERSION_11,
        "jvmTarget" to "11",
        "isMinifyEnabled" to true))
    set("version", mapOf(
        "" to ""
    ))
}