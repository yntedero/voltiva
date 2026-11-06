// Top-level build file. AGP 9 provides built-in Kotlin, so no kotlin-android plugin is applied.
// The Compose and serialization compiler plugins are declared here and applied in the app module.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}
