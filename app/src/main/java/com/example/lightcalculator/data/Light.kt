package com.example.lightcalculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Light(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val brand: String,
    val model: String,
    val power: Int,
    val DMXmode: Int,
    val DMXmodeDes: String
)