package com.project.hello.transit.agency.framework.internal.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.hello.transit.agency.framework.internal.datasource.db.TransitAgencyEntity

@Database(entities = [TransitAgencyEntity::class], version = 1, exportSchema = false)
internal abstract class TransitAgencyDatabase : RoomDatabase() {

    abstract fun transitAgencyDao(): TransitAgencyDao
}