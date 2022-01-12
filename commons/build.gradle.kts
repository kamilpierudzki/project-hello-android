import com.project.hello.script.Config
import com.project.hello.script.Dependencies

plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
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
            baseName = "commons"
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
                implementation(Dependencies.hilt)
                configurations.getByName("kapt")
                    .dependencies.add(Dependencies.hiltKaptCompiler)

                implementation(Dependencies.appcompat)
                implementation(Dependencies.material)
                implementation(Dependencies.coreKtx)

                implementation(Dependencies.kotlinxCoroutinesCore)
                implementation(Dependencies.kotlinxCoroutinesAndroid)

                implementation(Dependencies.rxAndroid)
                implementation(Dependencies.rxJava)

                implementation(Dependencies.gson)
                implementation(Dependencies.junit)
                implementation(Dependencies.mockitoKotlin)
                implementation(Dependencies.kotlinxCoroutinesTest)
            }
        }
        val androidTest by getting {
            dependencies {
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
}