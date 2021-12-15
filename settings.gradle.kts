pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "project-hello-multiplatform"
include(":android-app")
include(":welcome")
include(":top-level-resources")
include(":splash")
include(":legal")
include(":commons")
include(":settings")
include(":vehicle-prediction")
include(":transit-agency")
