package com.example.lightcalculator.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Light::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lightFixtureDao(): LightDao
}