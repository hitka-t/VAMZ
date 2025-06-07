package com.example.lightcalculator.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LightViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LightViewModel::class.java)) {
            return LightViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}