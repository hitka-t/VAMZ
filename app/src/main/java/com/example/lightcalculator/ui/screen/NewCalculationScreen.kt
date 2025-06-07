package com.example.lightcalculator.ui.screen

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lightcalculator.viewmodel.LightViewModel
import com.example.lightcalculator.viewmodel.LightViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCalculationScreen() {
    val context = LocalContext.current.applicationContext as Application
    val viewModel: LightViewModel = viewModel(factory = LightViewModelFactory(context))

    val brands by viewModel.brands.collectAsState()
    var selectedBrand by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Load brands once
    LaunchedEffect(Unit) {
        viewModel.loadBrands()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Nový výpočet", style = MaterialTheme.typography.titleLarge)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedBrand,
                onValueChange = {},
                readOnly = true,
                label = { Text("Značka") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                brands.forEach { brand ->
                    DropdownMenuItem(
                        text = { Text(brand) },
                        onClick = {
                            selectedBrand = brand
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}