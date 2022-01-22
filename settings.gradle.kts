rootProject.name = "colors2d"

pluginManagement {
    resolutionStrategy {
        repositories {
            gradlePluginPortal()
            mavenCentral()
        }
    }
}

include("shared")
include("client")
include("server")