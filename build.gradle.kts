// ─── Root build.gradle.kts ─────────────────────────────────────────────────────
plugins {
    id("com.android.application")                   version "8.5.2" apply false
    id("org.jetbrains.kotlin.android")              version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.compose")       version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0" apply false

    // KSP ‑ ***måste matcha Kotlin‑bas = 2.1.0***
    id("com.google.devtools.ksp")                   version "2.0.21-1.0.27" apply false
}

