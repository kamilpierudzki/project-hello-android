import com.project.hello.script.Config
import com.project.hello.script.Dependencies
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs")
    kotlin("multiplatform")
    kotlin("kapt")
}

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget = when {
        System.getenv("SDK_NAME")?.startsWith("iphoneos") == true -> ::iosArm64
        System.getenv("NATIVE_ARCH")?.startsWith("arm") == true -> ::iosSimulatorArm64
        else -> ::iosX64
    }

    iosTarget("ios") {
        binaries {
            framework {
                baseName = "splash"
            }
        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(project(":top-level-resources"))
                implementation(project(":commons"))
                implementation(project(":legal"))
                implementation(project(":welcome"))
                implementation(project(":vehicle-prediction"))
                implementation(project(":transit-agency"))

                implementation(Dependencies.material)
                implementation(Dependencies.appcompat)
                implementation(Dependencies.constraintLayout)

                implementation(Dependencies.hilt)
                configurations.getByName("kapt")
                    .dependencies.add(Dependencies.hiltKaptCompiler)

                implementation(Dependencies.navigationFragment)
                implementation(Dependencies.navigationUi)
            }
        }
        val androidTest by getting {
            dependencies {
            }
        }
        val iosMain by getting
        val iosTest by getting
    }
}

android {
    compileSdk = Config.compileSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
    }
    buildFeatures {
        viewBinding = true
    }
}
