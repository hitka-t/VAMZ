package com.example.lightcalculator.data.preset

import com.example.lightcalculator.data.AddedLight

data class Preset(
    val name: String,
    val lights: List<AddedLight>
)