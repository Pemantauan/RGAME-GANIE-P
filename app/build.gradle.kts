plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.rgame.ganiep"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rgame.ganiep"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }
}
