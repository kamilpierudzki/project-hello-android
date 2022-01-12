import com.project.hello.script.Config
import com.project.hello.script.Dependencies

plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

dependencies {
    implementation(project(":top-level-resources"))
    implementation(project(":commons"))
    implementation(project(":splash"))
    implementation(project(":legal"))
    implementation(project(":welcome"))
    implementation(project(":vehicle-prediction"))
    implementation(project(":transit-agency"))
    implementation(project(":settings"))

    implementation(Dependencies.multidex)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.material)

    implementation(Dependencies.navigationFragment)
    implementation(Dependencies.navigationUi)

    implementation(Dependencies.hilt)
    kapt(Dependencies.hiltCompiler)
}

android {
    compileSdk = Config.compileSdk
    defaultConfig {
        applicationId = "com.project.hello.android"
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        versionCode = Config.versionCode
        versionName = Config.versionName
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles("proguard-android-optimize.txt", "proguard-rules.pro")
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
    }
    buildFeatures {
        viewBinding = true
    }
    packagingOptions {
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
        resources.excludes.add("META-INF/atomicfu.kotlin_module")
        resources.excludes.add("META-INF/licenses/ASM")
        resources.excludes.add("win32-x86-64/attach_hotspot_windows.dll")
        resources.excludes.add("win32-x86/attach_hotspot_windows.dll")
    }
}