package com.example.lightcalculator.ui.screen

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lightcalculator.R
import com.example.lightcalculator.data.AddedLight
import com.example.lightcalculator.viewmodel.CalculationViewModel
import com.example.lightcalculator.viewmodel.LightViewModel
import com.example.lightcalculator.viewmodel.LightViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCalculationScreen(
    navController: NavHostController,
    sharedViewModel: CalculationViewModel
) {
    val application = LocalContext.current.applicationContext as Application
    val viewModel: LightViewModel = viewModel(factory = LightViewModelFactory(application))

    val brands by viewModel.brands.collectAsState()
    val lightTypes by viewModel.lightTypes.collectAsState()
    val dmxModes by viewModel.dmxModes.collectAsState()

    var selectedBrand by rememberSaveable { mutableStateOf("") }
    var selectedType by rememberSaveable { mutableStateOf("") }
    var selectedDmxMode by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("") }
    var addedItems by rememberSaveable { mutableStateOf(listOf<AddedLight>()) }

    var brandExpanded by rememberSaveable { mutableStateOf(false) }
    var typeExpanded by rememberSaveable { mutableStateOf(false) }
    var dmxModeExpanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadBrands()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = stringResource(R.string.title_new_calculation), style = MaterialTheme.typography.titleLarge)

            // Značka
            ExposedDropdownMenuBox(
                expanded = brandExpanded,
                onExpandedChange = { brandExpanded = !brandExpanded }
            ) {
                TextField(
                    value = selectedBrand,
                    onValueChange = {},
                    readOnly = true,
                    enabled = brands.isNotEmpty(),
                    label = { Text(stringResource(R.string.label_brand)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = brandExpanded) },
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
            ExposedDropdownMenuBox(
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = !typeExpanded }
            ) {
                TextField(
                    value = selectedType,
                    onValueChange = {},
                    readOnly = true,
                    enabled = selectedBrand.isNotEmpty() && lightTypes.isNotEmpty(),
                    label = { Text(stringResource(R.string.label_light_type)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
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

            // DMX mód
            ExposedDropdownMenuBox(
                expanded = dmxModeExpanded,
                onExpandedChange = { dmxModeExpanded = !dmxModeExpanded }
            ) {
                TextField(
                    value = selectedDmxMode,
                    onValueChange = {},
                    readOnly = true,
                    enabled = selectedType.isNotEmpty() && dmxModes.isNotEmpty(),
                    label = { Text(stringResource(R.string.label_dmx_mode)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dmxModeExpanded) },
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
                                quantity = ""
                                dmxModeExpanded = false
                            }
                        )
                    }
                }
            }

            // Počet
            OutlinedTextField(
                value = quantity,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        quantity = newValue
                    }
                },
                enabled = selectedDmxMode.isNotEmpty(),
                label = { Text(stringResource(R.string.label_quantity)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        // Scrollovateľný zoznam pridaných svetiel
        if (addedItems.isNotEmpty()) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .padding(top = 340.dp, bottom = 100.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = stringResource(R.string.summary_title),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                addedItems.forEach { item ->
                    Text(
                        text = stringResource(R.string.summary_format, item.quantity.toString(), item.brand, item.model),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }

        // Tlačidlá naspodku
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    if (selectedBrand.isNotEmpty() && selectedType.isNotEmpty()
                        && selectedDmxMode.isNotEmpty() && quantity.isNotEmpty()
                    ) {
                        val light = AddedLight(
                            quantity = quantity.toInt(),
                            brand = selectedBrand,
                            model = selectedType,
                            dmxModeDes = selectedDmxMode
                        )
                        addedItems = addedItems + light

                        selectedBrand = ""
                        selectedType = ""
                        selectedDmxMode = ""
                        quantity = ""
                        viewModel.clearLightTypes()
                        viewModel.clearDmxModes()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedBrand.isNotEmpty() && selectedType.isNotEmpty()
                        && selectedDmxMode.isNotEmpty() && quantity.isNotEmpty()
            ) {
                Text(stringResource(R.string.button_add))
            }

            Button(
                onClick = {
                    sharedViewModel.setItems(addedItems)
                    navController.navigate("result")
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = addedItems.isNotEmpty()
            ) {
                Text(stringResource(R.string.button_calculate))
            }
        }
    }
}
