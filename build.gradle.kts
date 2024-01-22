plugins {
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    kotlin("android").version(libs.versions.kotlinVersion).apply(false)
    kotlin("jvm").version(libs.versions.kotlinVersion).apply(false)
    alias(libs.plugins.daggerHilt).apply(false)
    alias(libs.plugins.ksp).apply(false)
}
