package com.project.hello.transit.agency.framework.internal.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TransitAgencyEntity::class], version = 1, exportSchema = false)
internal abstract class TransitAgencyDatabase : RoomDatabase() {

    abstract fun transitAgencyDao(): TransitAgencyDao
}