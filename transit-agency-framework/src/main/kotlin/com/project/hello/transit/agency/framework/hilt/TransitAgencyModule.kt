package com.project.hello.transit.agency.framework.hilt

import android.content.Context
import androidx.room.Room
import com.project.hello.transit.agency.framework.internal.db.TransitAgencyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class TransitAgencyModule {

    @Provides
    @Singleton
    fun provideTransitAgencyDatabase(@ApplicationContext appContext: Context): TransitAgencyDatabase =
        Room.databaseBuilder(
            appContext,
            TransitAgencyDatabase::class.java,
            "TransitAgencyDatabase"
        )
            .build()
}