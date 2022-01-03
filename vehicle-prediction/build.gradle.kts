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

                implementation("androidx.camera:camera-camera2:1.1.0-alpha11")
                implementation("androidx.camera:camera-lifecycle:1.1.0-alpha11")
                implementation("androidx.camera:camera-view:1.0.0-alpha28")

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

                implementation("com.google.android.gms:play-services-mlkit-text-recognition:17.0.1")
                api("com.google.android.gms:play-services-location:19.0.0")

                implementation("com.squareup.retrofit2:retrofit:2.9.0")
                implementation("com.squareup.retrofit2:converter-gson:2.9.0")

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
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
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
        buildConfigField(
            "String",
            "GOOGLE_MAPS_API_KEY",
            properties["GOOGLE_MAPS_API_KEY"].toString()
        )
    }
    buildFeatures {
        viewBinding = true
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}