package com.example.lightcalculator.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lightcalculator.viewmodel.LightViewModel
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.Text

@Composable
fun NewCalculationScreen(viewModel: LightViewModel = viewModel()) {
    // Stav vyberov
    var selectedBrand by remember { mutableStateOf("") }
    var selectedModel by remember { mutableStateOf("") }
    var selectedModeDes by remember { mutableStateOf("") }

    val brands by viewModel.brands.collectAsState()
    val models by viewModel.models.collectAsState()
    val modes by viewModel.modes.collectAsState()
    val selectedLight by viewModel.selectedLight.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Novy vypocet", style = MaterialTheme.typography.titleLarge)

        // BRAND
        DropdownMenuField(
            label = "Brand",
            options = brands,
            selected = selectedBrand,
            onSelect = {
                selectedBrand = it
                selectedModel = ""
                selectedModeDes = ""
                viewModel.loadModelsForBrand(it)
            }
        )

        // MODEL
        if (selectedBrand.isNotEmpty()) {
            DropdownMenuField(
                label = "Model",
                options = models,
                selected = selectedModel,
                onSelect = {
                    selectedModel = it
                    selectedModeDes = ""
                    viewModel.loadModesForModel(selectedBrand, it)
                }
            )
        }

        // MODE
        if (selectedModel.isNotEmpty()) {
            DropdownMenuField(
                label = "DMX Mode",
                options = modes,
                selected = selectedModeDes,
                onSelect = {
                    selectedModeDes = it
                    viewModel.loadLight(selectedBrand, selectedModel, it)
                }
            )
        }

        // ZOBRAZENIE DETAILOV
        selectedLight?.let { light ->
            Divider()
            Text("Vybrane svietidlo:")
            Text("Power: ${light.power} W")
            Text("DMX Mode: ${light.DMXmode}")
        }
    }
}
@Composable
fun DropdownMenuField(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label)
        Box {
            OutlinedTextField(
                value = selected,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                label = { Text(label) },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown icon"
                    )
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelect(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
