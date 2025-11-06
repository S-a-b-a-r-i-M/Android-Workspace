 plugins {
    alias(libs.plugins.android.application) // android.application plugin tells Gradle that you're building an Android app (not a library)
    alias(libs.plugins.kotlin.android) // kotlin.android plugin enables Kotlin language support in your Android project.
    alias(libs.plugins.kotlin.compose) // This is the **Kotlin Compiler Plugin for Jetpack Compose**
    alias(libs.plugins.kotlin.hilt)
    id("org.jetbrains.kotlin.plugin.parcelize") // But this is working
    //    alias(libs.plugins.kotlin.parcelize) // This is Not Working
    kotlin("kapt")
}

android {
    namespace = "com.example.firstapplication" // Used for generated code and R class.
    compileSdk = 36  /* this tells Gradle which version of the Android SDK to use when compiling your code.
    SDK 36 corresponds to a recent version of Android, giving you access to the latest APIs and features.
    */

    defaultConfig {
        applicationId = "com.example.firstapplication" // Used to identify your app on device & Play Store
        minSdk = 27
        targetSdk = 36 // For which Android SDK version the app was designed and tested for.
        versionCode = 1 // versionCode is for machines
        versionName = "1.0" // versionName is for humans

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // specifies which framework to use for running automated tests on actual devices or emulators.
    }

    buildTypes { // This defines different configurations for building your app
        release {
            isMinifyEnabled = false /*  means the build process won't shrink your code or remove unused parts
            (you'd typically enable this for production to reduce app size) */
            proguardFiles( // specify configuration files for code obfuscation and optimization.
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {
    // 1. implementation dependencies are included in your final app
    // 2. testImplementation are only used for unit tests running on your development machine
    // 3. androidTestImplementation are for tests running on Android devices
    // 4. debugImplementation are only included in debug builds
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // Force a compatible JavaPoet version
    constraints {
        implementation("com.squareup:javapoet:1.13.0") {
            because("Resolve JavaPoet version conflicts between annotation processors")
        }
    }

    // Room
    implementation(libs.androidx.room.runtime)
    // ksp(libs.androidx.room.compiler)
    kapt(libs.androidx.room.compiler)
    // annotationProcessor("androidx.room:room-compiler:2.7.2") The issue: You’re coding in Kotlin, but annotationProcessor is for Java.

    // HILT
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // For image loading and caching
    implementation("com.github.bumptech.glide:glide:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.15.1")

    // For HTTP requests
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    // For coroutines (recommended for async operations)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.2")

    // ViewModel and lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.2")

    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}