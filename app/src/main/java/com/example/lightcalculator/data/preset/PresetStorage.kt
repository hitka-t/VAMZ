package com.example.lightcalculator.data.preset

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object PresetStorage {
    private const val PRESET_FILE = "presets.json"

    private val gson = Gson()

    fun savePreset(context: Context, preset: Preset) {
        val current = loadPresets(context).toMutableList()
        current.add(preset)
        val json = gson.toJson(current)
        File(context.filesDir, PRESET_FILE).writeText(json)
    }

    fun loadPresets(context: Context): List<Preset> {
        val file = File(context.filesDir, PRESET_FILE)
        if (!file.exists()) return emptyList()

        val json = file.readText()
        val type = object : TypeToken<List<Preset>>() {}.type
        return gson.fromJson(json, type)
    }
}
