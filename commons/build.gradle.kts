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
                implementation("com.google.dagger:hilt-android:2.38.1")
                configurations.getByName("kapt").dependencies.add(
                    org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency(
                        "com.google.dagger",
                        "hilt-compiler",
                        "2.38.1"
                    )
                )

                implementation("androidx.appcompat:appcompat:1.4.0")
                implementation("com.google.android.material:material:1.4.0")
                implementation("androidx.core:core-ktx:1.7.0")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")

                implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
                implementation("io.reactivex.rxjava3:rxjava:3.0.0")

                implementation("com.google.code.gson:gson:2.8.6")
                implementation("junit:junit:4.13.2")
                implementation("org.mockito.kotlin:mockito-kotlin:3.2.0")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.3")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
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
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 23
        targetSdk = 31
    }
}