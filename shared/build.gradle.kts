plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

val ktorVersion = project.property("ktor.version") as String

kotlin {
    jvm()
    js {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-html-builder:$ktorVersion")
            }
        }
    }
}