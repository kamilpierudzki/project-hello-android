import com.project.hello.script.Config
import com.project.hello.script.Dependencies

plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs")
    kotlin("multiplatform")
    kotlin("kapt")
}

kotlin {
    android()

    listOf(
        iosX64(),
        iosArm64(),
        //iosSimulatorArm64() sure all ios dependencies support this target
    ).forEach {
        it.binaries.framework {
            baseName = "vehicle-prediction"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":commons"))
                implementation(project(":transit-agency"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(project(":commons"))
                implementation(project(":transit-agency"))
                implementation(project(":settings"))

                implementation(Dependencies.cameraCamera2)
                implementation(Dependencies.cameraLifecycle)
                implementation(Dependencies.cameraView)

                implementation(Dependencies.navigationFragment)
                implementation(Dependencies.navigationUi)

                implementation(Dependencies.hilt)
                configurations.getByName("kapt")
                    .dependencies.add(Dependencies.hiltKaptCompiler)

                implementation(Dependencies.playServicesMlkitTextRecognition)
                api(Dependencies.playServicesLocation)

                implementation(Dependencies.retrofit)
                implementation(Dependencies.retrofitConverter)

                implementation(Dependencies.rxAndroid)
                implementation(Dependencies.rxJava)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(Dependencies.junit)
                implementation(Dependencies.mockitoKotlin)
                implementation(Dependencies.mockitoInline)
                implementation(Dependencies.coreTesting)
                implementation(Dependencies.kotlinxCoroutinesTest)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        //val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            //iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        //val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            //iosSimulatorArm64Test.dependsOn(this)
        }
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
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}