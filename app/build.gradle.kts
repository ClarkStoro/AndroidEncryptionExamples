plugins {
    alias(libs.plugins.androidApplication)
    kotlin("android").version(libs.versions.kotlinVersion)
    kotlin("kapt")
    alias(libs.plugins.daggerHilt)
}

android {
    namespace = "com.clarkstoro.androidencryptionexamples"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.clarkstoro.androidencryptionexamples"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 2
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
    }
}

dependencies {
    // Data module.
    implementation(project(":data"))

    implementation(libs.bundles.appDependencies)
    testImplementation(libs.jUnitDependency)
    androidTestImplementation(libs.bundles.androidTestDependencies)
    debugImplementation(libs.bundles.debugDependencies)

    // Navigation Compose.
    implementation(libs.navigationDependency)

    // Coroutines.
    implementation(libs.bundles.coroutinesDependencies)

    // Hilt.
    implementation(libs.bundles.hiltDependencies)
    kapt(libs.hiltCompiler)

    // Landscapist-Glide.
    implementation(libs.landscapistGlideDependency)

    // Splash Screen.
    implementation(libs.splashScreenDependency)

    // Timber.
    implementation(libs.timberDependency)

    // Location Services.
    implementation(libs.locationDependency)

    // Jetpack Compose Permissions.
    implementation(libs.permissionsComposeDependency)

    // Compose System UI Controller (for system bars colors)
    implementation(libs.systemUiController)

    implementation(libs.security.crypto)
    implementation(libs.biometric)

    implementation(libs.lifecycleCompose)
}
