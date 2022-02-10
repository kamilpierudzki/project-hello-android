package com.project.hello.script

import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency

object Dependencies {
    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    val hiltKaptCompiler = DefaultExternalModuleDependency(
        "com.google.dagger",
        "hilt-compiler",
        Versions.hilt
    )
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"

    const val cameraCamera2 = "androidx.camera:camera-camera2:${Versions.cameraCamera2}"
    const val cameraLifecycle = "androidx.camera:camera-lifecycle:${Versions.cameraLifecycle}"
    const val cameraView = "androidx.camera:camera-view:${Versions.cameraView}"

    const val playServicesMlkitTextRecognition =
        "com.google.android.gms:play-services-mlkit-text-recognition:${Versions.playServicesMlkitTextRecognition}"
    const val playServicesLocation =
        "com.google.android.gms:play-services-location:${Versions.playServicesLocation}"
    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val rxAndroid = "io.reactivex.rxjava3:rxandroid:${Versions.rxJava}"
    const val rxJava = "io.reactivex.rxjava3:rxjava:${Versions.rxJava}"
    const val junit = "junit:junit:${Versions.junit}"
    const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:${Versions.mockitoKotlin}"
    const val mockitoInline = "org.mockito:mockito-inline:${Versions.mockitoInline}"
    const val coreTesting = "androidx.arch.core:core-testing:${Versions.coreTesting}"
    const val kotlinxCoroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinxCoroutinesTest}"

    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    val roomKaptCompiler = DefaultExternalModuleDependency(
        "androidx.room",
        "room-compiler",
        Versions.room
    )

    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val kotlinxCoroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinxCoroutinesCore}"
    const val kotlinxCoroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinxCoroutinesAndroid}"
    const val multidex = "androidx.multidex:multidex:${Versions.multidex}"
    const val firebaseAnalyticsKtx = "com.google.firebase:firebase-analytics-ktx:${Versions.firebaseAnalyticsKtx}"
}