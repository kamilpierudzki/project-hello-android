package com.project.hello.transit.agency.framework.internal.repository.db

import androidx.room.*

@Dao
internal interface TransitAgencyDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransitAgency(city: TransitAgencyEntity)

    @Transaction
    @Query("DELETE FROM TransitAgencyEntity")
    fun deleteAll()

    @Transaction
    @Query("SELECT * FROM TransitAgencyEntity ORDER BY id ASC")
    fun getTransitAgencies(): List<TransitAgencyEntity>
}