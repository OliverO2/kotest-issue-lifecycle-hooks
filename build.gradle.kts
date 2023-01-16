@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform")
    id("io.kotest.multiplatform") version "5.5.4"
    application
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(11)

    jvm {
        withJava() // WORKAROUND https://youtrack.jetbrains.com/issue/KT-42683 – "Could not find or load main class"
    }

    js(IR) {
        useCommonJs()
        binaries.executable()
        browser()
    }

    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation("io.kotest:kotest-framework-engine:5.5.4")
                implementation("io.kotest:kotest-assertions-core:5.5.4")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("io.kotest:kotest-runner-junit5:5.5.4")
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}
