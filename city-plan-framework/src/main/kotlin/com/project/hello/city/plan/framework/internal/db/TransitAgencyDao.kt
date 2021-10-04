package com.project.hello.city.plan.framework.internal.db

import androidx.room.*
import com.project.hello.city.plan.framework.internal.datasource.db.TransitAgencyEntity

@Dao
internal interface TransitAgencyDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransitAgency(city: TransitAgencyEntity)

    @Transaction
    @Query("DELETE FROM TransitAgencyEntity")
    fun deleteAll()

    @Transaction
    @Query("SELECT * FROM TransitAgencyEntity ORDER BY cityPlanId ASC")
    fun getTransitAgencies(): List<TransitAgencyEntity>
}