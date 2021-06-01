package com.project.hallo.city.plan.framework.hilt

import android.content.Context
import androidx.room.Room
import com.project.hallo.city.plan.framework.internal.datasource.db.CityDatabaseConverters
import com.project.hallo.city.plan.framework.internal.db.CityDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class CityPlanModule {

    @Provides
    @Singleton
    fun provideCityDatabase(@ApplicationContext appContext: Context): CityDatabase =
        Room.databaseBuilder(
            appContext,
            CityDatabase::class.java,
            "CityDatabase"
        )
            .build()
}