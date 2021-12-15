package com.project.hello.commons.hilt

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.project.hello.commons.concurrency.Synchronization
import com.project.hello.commons.internal.concurrency.SynchronizationImpl
import com.project.hello.commons.internal.time.SystemTimeImpl
import com.project.hello.commons.time.SystemTime
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CommonsModule {

    @Binds
    abstract fun bindSynchronization(impl: SynchronizationImpl): Synchronization

    @Binds
    abstract fun bindSystemTime(impl: SystemTimeImpl): SystemTime

    companion object {
        @Provides
        fun provideResources(@ApplicationContext appContext: Context): Resources =
            appContext.resources

        @IoDispatcher
        @Provides
        fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

        @DefaultDispatcher
        @Provides
        fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

        @Provides
        @AppSharedPreferences
        @Singleton
        fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences =
            appContext.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    }
}