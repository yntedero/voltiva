// Top-level build file. AGP 9 provides built-in Kotlin, so no kotlin-android plugin is applied.
// Declaring the Kotlin compiler plugins here puts Kotlin Gradle plugin 2.4.x on the build classpath.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
}
