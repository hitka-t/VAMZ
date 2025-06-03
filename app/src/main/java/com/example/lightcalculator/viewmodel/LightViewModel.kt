package com.example.lightcalculator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lightcalculator.data.Database
import com.example.lightcalculator.data.Light
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LightViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = Database.getDatabase(application).lightFixtureDao()

    private val _brands = MutableStateFlow<List<String>>(emptyList())
    val brands: StateFlow<List<String>> = _brands

    private val _models = MutableStateFlow<List<String>>(emptyList())
    val models: StateFlow<List<String>> = _models

    private val _modes = MutableStateFlow<List<String>>(emptyList())
    val modes: StateFlow<List<String>> = _modes

    private val _selectedLight = MutableStateFlow<Light?>(null)
    val selectedLight: StateFlow<Light?> = _selectedLight

    // Nacita znacky pri spusteni
    init {
        viewModelScope.launch {
            _brands.value = dao.getAllBrands()
        }
    }

    fun loadModelsForBrand(brand: String) {
        viewModelScope.launch {
            _models.value = dao.getModelsForBrand(brand)
        }
    }

    fun loadModesForModel(brand: String, model: String) {
        viewModelScope.launch {
            _modes.value = dao.getModesForModel(brand, model)
        }
    }

    fun loadLight(brand: String, model: String, modeDes: String) {
        viewModelScope.launch {
            _selectedLight.value = dao.getLightByAllParams(brand, model, modeDes)
        }
    }
}