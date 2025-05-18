extra.apply{
    set("android", mapOf(
        "compileSdk"                    to 35,
        "minSdk"                        to 26,
        "targetSdk"                     to 35,
        "versionCode"                   to 1,
        "versionName"                   to "1.0",
        "sourceCompatibility"           to JavaVersion.VERSION_21,
        "targetCompatibility"           to JavaVersion.VERSION_21,
        "jvmTarget"                     to "21",
        "isMinifyEnabled"               to true))

    set("version", mapOf(
        "autoServiceVersion"            to "1.1.1",
        "autoServiceCompileVersion"     to "1.0-rc3",
        "annotationApi"                 to "1.3.2"
    ))
}