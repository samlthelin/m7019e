pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("com\\.google\\.devtools.*")   //  << added
                includeGroupByRegex("androidx.*")
                includeGroup("com.google.devtools.ksp")   // <—  **new**

            }
        }
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        id("org.jetbrains.kotlin.android") version "2.0.21" apply false
        id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21" apply false
        id("com.google.devtools.ksp") version "2.0.21-1.0.20" apply false
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "laboration1"
include(":app")
