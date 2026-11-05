plugins {
    id("com.android.application")
}

android {
    namespace = "pl.appoee.panel"
    compileSdk = 35

    defaultConfig {
        applicationId = "pl.appoee.panel"
        minSdk = 23
        targetSdk = 35
        versionCode = providers.gradleProperty("appVersionCode").orElse("4").get().toInt()
        versionName = providers.gradleProperty("appVersionName").orElse("2.2.0").get()
    }

    val releaseKeyPath = System.getenv("ANDROID_KEYSTORE_PATH")
    signingConfigs {
        if (!releaseKeyPath.isNullOrBlank()) {
            create("githubRelease") {
                storeFile = file(releaseKeyPath)
                storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("ANDROID_KEY_ALIAS")
                keyPassword = System.getenv("ANDROID_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            if (!releaseKeyPath.isNullOrBlank()) {
                signingConfig = signingConfigs.getByName("githubRelease")
            }
        }
    }
}
