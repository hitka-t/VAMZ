package com.example.lightcalculator.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lightcalculator.data.AddedLight
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.suspendCancellableCoroutine

class CalculationViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<AddedLight>>(emptyList())
    val items: StateFlow<List<AddedLight>> = _items

    fun setItems(list: List<AddedLight>) {
        _items.value = list
    }

    suspend fun getPowerForLight(brand: String, model: String, dmxDes: String): Int {
        return suspendCancellableCoroutine { cont ->
            FirebaseFirestore.getInstance().collection("lights")
                .whereEqualTo("brand", brand)
                .whereEqualTo("model", model)
                .whereEqualTo("dmxmodeDes", dmxDes)
                .get()
                .addOnSuccessListener { result ->
                    val power = result.documents.firstOrNull()?.getLong("power")?.toInt() ?: 0
                    cont.resume(power, null)
                }
                .addOnFailureListener {
                    cont.resume(0, null)
                }
        }
    }

    suspend fun getDmxSize(brand: String, model: String, dmxDes: String): Int {
        return suspendCancellableCoroutine { cont ->
            FirebaseFirestore.getInstance().collection("lights")
                .whereEqualTo("brand", brand)
                .whereEqualTo("model", model)
                .whereEqualTo("dmxmodeDes", dmxDes)
                .get()
                .addOnSuccessListener { result ->
                    val value = result.documents.firstOrNull()?.getLong("DMXmode")?.toInt() ?: 0
                    cont.resume(value, null)
                }
                .addOnFailureListener {
                    cont.resume(0, null)
                }
        }
    }
}