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
                baseName = "shared"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":commons"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(project(":commons"))

                implementation("androidx.core:core-ktx:1.7.0")

                implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
                implementation("androidx.navigation:navigation-ui-ktx:2.3.5")

                implementation("com.google.dagger:hilt-android:2.38.1")
                configurations.getByName("kapt").dependencies.add(
                    org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency(
                        "com.google.dagger",
                        "hilt-compiler",
                        "2.38.1"
                    )
                )

                implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
                implementation("io.reactivex.rxjava3:rxjava:3.0.0")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
                implementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
                implementation("org.mockito:mockito-inline:4.2.0")
                implementation("androidx.arch.core:core-testing:2.1.0")
            }
        }
        val iosMain by getting
        val iosTest by getting
    }
}

android {
    compileSdkVersion(31)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        minSdkVersion(23)
        targetSdkVersion(31)
    }

    buildFeatures {
        viewBinding = true
    }
}