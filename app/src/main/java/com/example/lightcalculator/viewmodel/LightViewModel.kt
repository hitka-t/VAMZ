package com.example.lightcalculator.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lightcalculator.data.Light
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LightViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()

    // brand
    private val _brands = MutableStateFlow<List<String>>(emptyList())
    val brands: StateFlow<List<String>> = _brands
    // model
    private val _lightTypes = MutableStateFlow<List<String>>(emptyList())
    val lightTypes: StateFlow<List<String>> = _lightTypes
    //dmx mode
    private val _dmxModes = MutableStateFlow<List<String>>(emptyList())
    val dmxModes: StateFlow<List<String>> = _dmxModes

    // nacita vsetky znakcy
    fun loadBrands() {
        viewModelScope.launch {
            db.collection("lights").get()
                .addOnSuccessListener { result ->
                    val brandList = result.documents.mapNotNull { it.getString("brand") }
                    _brands.value = brandList.distinct().sorted()

                    Log.d("Firestore", "Brands loaded: ${_brands.value}")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error loading brands", e)
                }
        }
    }
    // na zaklade vybreatej znacky vyfiltruje len tie ktore su takej znacky
    fun loadLightTypesForBrand(brand: String) {
        viewModelScope.launch {
            db.collection("lights")
                .whereEqualTo("brand", brand)
                .get()
                .addOnSuccessListener { result ->
                    val modelList = result.documents.mapNotNull { it.getString("model") }
                    _lightTypes.value = modelList.distinct().sorted()

                    Log.d("Firestore", "Models for $brand: ${_lightTypes.value}")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error loading models", e)
                }
        }
    }
    // na zajklade znacky a modelu vyberie dmx mode
    fun loadDmxModesFor(brand: String, model: String) {
        viewModelScope.launch {
            db.collection("lights")
                .whereEqualTo("brand", brand)
                .whereEqualTo("model", model)
                .get()
                .addOnSuccessListener { result ->
                    val dmxDescriptions = result.documents.mapNotNull { doc ->
                        val light = doc.toObject(Light::class.java)
                        light?.dmxmodeDes
                    }.distinct().sorted()

                    _dmxModes.value = dmxDescriptions
                    Log.d("DMX", "Loaded DMX descriptions: $dmxDescriptions")
                }
                .addOnFailureListener { e ->
                    Log.e("DMX", "Failed to load DMX modes", e)
                }
        }
    }

    fun clearLightTypes() {
        _lightTypes.value = emptyList()
    }

    fun clearDmxModes() {
        _dmxModes.value = emptyList()
    }
}