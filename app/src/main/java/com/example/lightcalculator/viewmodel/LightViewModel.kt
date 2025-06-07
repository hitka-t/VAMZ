package com.example.lightcalculator.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LightViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()

    private val _brands = MutableStateFlow<List<String>>(emptyList())
    val brands: StateFlow<List<String>> = _brands

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
}