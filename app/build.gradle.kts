// ─── app/build.gradle.kts ──────────────────────────────────────────────────────
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")            // utan version
    id("org.jetbrains.kotlin.plugin.compose")     // utan version
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
}

/* ----------  GLOBAL RESOLUTION STRATEGY + EXCLUDES  ---------- */
configurations.all {
    resolutionStrategy {
        force("com.google.auto.value:auto-value:1.10.3")
        force("com.google.auto.value:auto-value-annotations:1.10.3")
    }
    exclude(group = "com.intellij", module = "annotations")
}

/* ----------  ANDROID CONFIG  ---------- */
android {
    namespace  = "com.example.laboration1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.laboration1"
        minSdk        = 24
        targetSdk     = 35
        versionCode   = 1
        versionName   = "1.0"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions { jvmTarget = "11" }

    buildFeatures { compose = true }

    packagingOptions {
        resources { pickFirst("messages/JavaOptionBundle.properties") }
    }
}

/* ----------  DEPENDENCIES  ---------- */
dependencies {
    // Compose + Navigation
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    // Image loading
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // ConstraintLayout for Compose
    implementation(libs.androidx.constraintlayout)

    // ----------  ROOM 2.6.1 ----------
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")   // generates MovieDatabase_Impl

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    // Networking & JSON
    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)
    // Media3 / ExoPlayer
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    // ----------  TESTING ----------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
