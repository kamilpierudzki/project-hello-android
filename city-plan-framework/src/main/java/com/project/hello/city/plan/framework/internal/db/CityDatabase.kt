package com.project.hello.city.plan.framework.internal.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.hello.city.plan.framework.internal.datasource.db.CityPlanEntity

@Database(entities = [CityPlanEntity::class], version = 1, exportSchema = false)
internal abstract class CityDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao
}