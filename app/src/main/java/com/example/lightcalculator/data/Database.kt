package com.example.lightcalculator.data

import android.content.Context
import androidx.room.Room

object Database {
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "light_fixture_db"
            ).fallbackToDestructiveMigration().build()
        }
        return instance!!
    }
}