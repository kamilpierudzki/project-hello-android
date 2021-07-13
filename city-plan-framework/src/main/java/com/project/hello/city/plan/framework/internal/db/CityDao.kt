package com.project.hello.city.plan.framework.internal.db

import androidx.room.*
import com.project.hello.city.plan.framework.internal.datasource.db.CityPlanEntity

@Dao
internal interface CityDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(city: CityPlanEntity)

    @Transaction
    @Query("DELETE FROM CityPlanEntity")
    fun deleteAll()

    @Transaction
    @Query("SELECT * FROM CityPlanEntity ORDER BY cityPlanId ASC")
    fun getCities(): List<CityPlanEntity>
}