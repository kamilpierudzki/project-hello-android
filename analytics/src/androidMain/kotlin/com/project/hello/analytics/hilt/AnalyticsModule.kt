package com.project.hello.analytics.hilt

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.project.hello.analytics.api.ScreenLogging
import com.project.hello.analytics.internal.ScreenLoggingImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {

    @Binds
    abstract fun bindScreenLogging(impl: ScreenLoggingImpl): ScreenLogging

    companion object {

        @Provides
        fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics
    }
}