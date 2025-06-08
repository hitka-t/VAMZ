package com.example.lightcalculator.ui.screen

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.lightcalculator.R

import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lightcalculator.viewmodel.LightViewModel
import com.example.lightcalculator.viewmodel.LightViewModelFactory



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCalculationScreen() {
    val application = LocalContext.current.applicationContext as Application
    val context = LocalContext.current
    val viewModel: LightViewModel = viewModel(factory = LightViewModelFactory(application))

    val brands by viewModel.brands.collectAsState()
    val lightTypes by viewModel.lightTypes.collectAsState()
    val dmxModes by viewModel.dmxModes.collectAsState()

    var selectedBrand by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }
    var selectedDmxMode by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var addedItems by remember { mutableStateOf(listOf<String>()) }

    var brandExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }
    var dmxModeExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadBrands()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp), // priestor pre spodné tlačidlá
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource(R.string.title_new_calculation))

            // Značka
            ExposedDropdownMenuBox(
                expanded = brandExpanded,
                onExpandedChange = { brandExpanded = !brandExpanded }
            ) {
                TextField(
                    value = selectedBrand,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.label_brand)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = brandExpanded)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = brandExpanded,
                    onDismissRequest = { brandExpanded = false }
                ) {
                    brands.forEach { brand ->
                        DropdownMenuItem(
                            text = { Text(brand) },
                            onClick = {
                                selectedBrand = brand
                                selectedType = ""
                                selectedDmxMode = ""
                                quantity = ""
                                viewModel.loadLightTypesForBrand(brand)
                                brandExpanded = false
                            }
                        )
                    }
                }
            }

            // Model
            if (lightTypes.isNotEmpty()) {
                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = !typeExpanded }
                ) {
                    TextField(
                        value = selectedType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.label_light_type)) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }
                    ) {
                        lightTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    selectedType = type
                                    selectedDmxMode = ""
                                    quantity = ""
                                    viewModel.loadDmxModesFor(selectedBrand, selectedType)
                                    typeExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // DMX mód
            if (dmxModes.isNotEmpty()) {
                ExposedDropdownMenuBox(
                    expanded = dmxModeExpanded,
                    onExpandedChange = { dmxModeExpanded = !dmxModeExpanded }
                ) {
                    TextField(
                        value = selectedDmxMode,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.label_dmx_mode)) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = dmxModeExpanded)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = dmxModeExpanded,
                        onDismissRequest = { dmxModeExpanded = false }
                    ) {
                        dmxModes.forEach { mode ->
                            DropdownMenuItem(
                                text = { Text(mode) },
                                onClick = {
                                    selectedDmxMode = mode
                                    dmxModeExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // pocet svetiel
            if (selectedBrand.isNotEmpty() && selectedType.isNotEmpty() && selectedDmxMode.isNotEmpty()) {
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            quantity = newValue
                        }
                    },
                    label = { Text(stringResource(R.string.label_quantity)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            // Zhrnutie po pridaní
            if (addedItems.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.summary_title),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )

                addedItems.forEach { item ->
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }

        // tlacitka dole obrazovky
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    if (selectedBrand.isNotEmpty() && selectedType.isNotEmpty()
                        && selectedDmxMode.isNotEmpty() && quantity.isNotEmpty()
                    ) {
                        val item = context.getString(
                            R.string.summary_format,
                            quantity,
                            selectedBrand,
                            selectedType
                        )
                        addedItems = addedItems + item

                        // Reset výberu
                        selectedBrand = ""
                        selectedType = ""
                        selectedDmxMode = ""
                        quantity = ""
                        viewModel.clearLightTypes()
                        viewModel.clearDmxModes()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.button_add))
            }

            Button(
                onClick = {
                    // budúca navigácia
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.button_calculate))
            }
        }
    }
}
