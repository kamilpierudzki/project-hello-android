package com.project.hello.analytics.internal

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.project.hello.analytics.api.ScreenLogging
import javax.inject.Inject

internal class ScreenLoggingImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
): ScreenLogging {

    override fun logScreen(name: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, name)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        }
    }
}