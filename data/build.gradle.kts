plugins {
    alias(libs.plugins.androidLibrary)
    kotlin("android").version(libs.versions.kotlinVersion)
    kotlin("kapt")
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.clarkstoro.data"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        consumerProguardFiles("consumer-rules.pro")

        // TODO: Insert your base URL here.
        buildConfigField("String", "BASE_URL", "\"https://insertyourbaseurlhere.com\"")
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
        buildConfig = true
    }
}

dependencies {
    // Domain module.
    api(project(":domain"))

    implementation(libs.kotlinDependency)

    // Inject.
    implementation(libs.injectDependency)

    // Preferences DataStore.
    implementation(libs.prefDataStoreDependency)

    // Material.
    implementation(libs.bundles.materialDependencies)

    // Retrofit.
    implementation(libs.bundles.retrofitDependencies)

    // Hilt.
    implementation(libs.bundles.hiltDependencies)
    kapt(libs.hiltCompiler)

    // Room.
    implementation(libs.roomRuntime)
    annotationProcessor(libs.roomCompiler)
    ksp(libs.roomCompiler)
}
