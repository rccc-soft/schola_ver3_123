plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.korekore"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.korekore"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packagingOptions {
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/INDEX.LIST")
        exclude("META-INF/DEPENDENCIES")
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.sun.mail:android-mail:1.6.7")
    implementation ("com.sun.mail:android-activation:1.6.7")
}