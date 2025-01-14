// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("org.jetbrains.kotlin.android") version "1.5.21" apply false
}
// build.gradle.kts
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}