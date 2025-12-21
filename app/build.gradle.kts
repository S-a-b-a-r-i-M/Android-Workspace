 plugins {
    alias(libs.plugins.android.application) // android.application plugin tells Gradle that you're building an Android app (not a library)
    alias(libs.plugins.kotlin.android) // kotlin.android plugin enables Kotlin language support in your Android project.
    alias(libs.plugins.kotlin.compose) // This is the **Kotlin Compiler Plugin for Jetpack Compose**
    alias(libs.plugins.kotlin.hilt)
    id("org.jetbrains.kotlin.plugin.parcelize") // But this is working
    //    alias(libs.plugins.kotlin.parcelize) // This is Not Working
    kotlin("kapt")
}
/* Just A Custom Plugin */
apply<CustomPlugin>()

class CustomPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("Hello Tech Universe !!! I'm a custom plugin😁")
    }
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


    println("<------------ flavorDimensions: $flavorDimensions")
    flavorDimensions += listOf("paidStatus", "style")
    productFlavors {
        create("free") {
            applicationIdSuffix = ".free" /* applicationIdSuffix allows you to install
            - multiple variants of the same app on one device simultaneously.
            Practical benefit: Developers can test release builds without losing their debug build with test data.
            Testers can compare multiple versions without switching.
            */
            dimension = "paidStatus"
        }

        create("paid") {
            applicationIdSuffix = ".paid"
            dimension = "paidStatus"
        }

        create("blue") {
            dimension = "style"
        }
        create("violet") {
            dimension = "style"
        }
    }

    buildTypes { // This defines different configurations for building your app
        debug {
            isMinifyEnabled = false  /*  means the build process won't shrink your code or remove unused parts
            (you'd typically enable this for production to reduce app size and prevent security risks of reverse engineering) */
            buildConfigField("String", "BASE_URL", "\"https://dev.com\"")
        }

        create("staging") { // Let's Assume We Need To Ship The APK to Testing Team But No Need For Some Release Level Build Processes
            // You Can See This Variant In Build Variants
            isMinifyEnabled = true
            buildConfigField("String", "BASE_URL", "\"https://dev.com\"")
            signingConfig = signingConfigs.getByName("debug")
        }

        release {
            isMinifyEnabled = true
            proguardFiles( // specify configuration files for code obfuscation and optimization.
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://release.com\"")
            signingConfig = signingConfigs.getByName("debug")
            enableAndroidTestCoverage = false
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
        buildConfig = true
    }
}

dependencies {
    // 1. implementation dependencies are included in your final app
    // 2. testImplementation are only used for unit tests running on your development machine
    // 3. androidTestImplementation are for tests running on Android devices
    // 4. debugImplementation are only included in debug builds
    implementation(project(":mylibrary"))

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

    // androidx.preference
    implementation("androidx.preference:preference:1.2.1")

    // Test
    // Testing-only dependencies
/*
    // Kotlin standard library - Provides Kotlin language features in tests
    androidTestImplementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")

    // AndroidX Test Core - Core testing utilities (ApplicationProvider, ActivityScenario)
    androidTestImplementation("androidx.test:core:1.5.0")

    // AndroidX Test Core KTX - Kotlin extensions for cleaner test code
    androidTestImplementation("androidx.test:core-ktx:1.5.0")

    // AndroidX JUnit - Provides AndroidJUnit4 runner to run tests on Android devices/emulators
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    // AndroidX JUnit KTX - Kotlin extensions for JUnit (e.g., ActivityScenarioRule)
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")

    // Test Runner - Executes instrumented tests on Android
    androidTestImplementation("androidx.test:runner:1.5.2")

    // Espresso Core - UI testing framework for interacting with views (clicks, typing, assertions)
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // --- Local Unit Tests (run on JVM, faster) ---

    // Test Core - Enables using Android Test APIs in local unit tests
    testImplementation("androidx.test:core:1.5.0")

    // Test JUnit - Allows using AndroidJUnit4 in Robolectric tests
    testImplementation("androidx.test.ext:junit:1.1.5")

    // JUnit 4 - Core unit testing framework with @Test, assertions, etc.
    testImplementation("junit:junit:4.13.2")

    // Robolectric - Simulates Android framework for fast local unit tests without emulator
    testImplementation("org.robolectric:robolectric:4.11.1")

    // Espresso Core - For testing UI logic in Robolectric tests
    testImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Espresso Intents - Mock and verify intents in Robolectric tests
    testImplementation("androidx.test.espresso:espresso-intents:3.5.1")

    // Truth - Fluent assertion library for more readable tests (e.g., assertThat(x).isEqualTo(y))
    testImplementation("androidx.test.ext:truth:1.5.0")

 */

    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.3")
    androidTestImplementation("androidx.test:runner:1.6.2")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
    implementation("androidx.test.espresso:espresso-idling-resource:3.6.1") //Will Be Used In App Code
    androidTestImplementation("androidx.test.espresso:espresso-idling-resource:3.6.1")
}