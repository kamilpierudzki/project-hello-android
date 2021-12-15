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

                implementation("com.google.android.material:material:1.4.0")
                implementation("androidx.appcompat:appcompat:1.3.1")
                implementation("androidx.constraintlayout:constraintlayout:2.1.1")

                implementation("com.google.dagger:hilt-android:2.38.1")
                configurations.getByName("kapt").dependencies.add(
                    org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency(
                        "com.google.dagger",
                        "hilt-compiler",
                        "2.38.1"
                    )
                )

                implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
                implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by getting
        val iosTest by getting
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 23
        targetSdk = 31
    }
    buildFeatures {
        viewBinding = true
    }
}
