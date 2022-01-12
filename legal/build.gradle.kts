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
            baseName = "legal"
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

                implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
                implementation("androidx.navigation:navigation-ui-ktx:2.3.5")

                implementation(Dependencies.hilt)
                configurations.getByName("kapt").dependencies.add(Dependencies.hiltKaptCompiler)

                implementation("com.google.code.gson:gson:2.8.6")

                implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
                implementation("io.reactivex.rxjava3:rxjava:3.0.0")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(project(":top-level-resources"))

                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
                implementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
                implementation("androidx.arch.core:core-testing:2.1.0")
                implementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
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

    buildFeatures {
        viewBinding = true
    }
}