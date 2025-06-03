package com.example.lightcalculator.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LightDao   {

    @Insert
    suspend fun insert(fixture: Light)

    @Query("SELECT DISTINCT brand FROM Light")
    suspend fun getAllBrands(): List<String>

    @Query("SELECT DISTINCT model FROM Light WHERE brand = :brand")
    suspend fun getModelsForBrand(brand: String): List<String>

    @Query("SELECT DISTINCT DMXmodeDes FROM Light WHERE brand = :brand AND model = :model")
    suspend fun getModesForModel(brand: String, model: String): List<String>

    @Query("SELECT * FROM Light WHERE brand = :brand AND model = :model AND DMXmodeDes = :modeDes LIMIT 1")
    suspend fun getLightByAllParams(brand: String, model: String, modeDes: String): Light
}