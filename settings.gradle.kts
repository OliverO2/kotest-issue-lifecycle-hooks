pluginManagement {
    plugins {
        kotlin("multiplatform") version "1.7.20" apply false
    }
    repositories {
        maven(url = "${System.getenv("HOME")!!}/.m2/local-repository")
        gradlePluginPortal()
    }
}

rootProject.name = "kotlin-multiplatform-arena"
