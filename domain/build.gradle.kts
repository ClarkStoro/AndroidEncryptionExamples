plugins {
    id("java-library")
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlinDependency)

    // Inject.
    implementation(libs.injectDependency)

    // Coroutines.
    implementation(libs.bundles.coroutinesDependencies)
}
