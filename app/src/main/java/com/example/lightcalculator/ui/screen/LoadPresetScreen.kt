package com.example.lightcalculator.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lightcalculator.R
import com.example.lightcalculator.data.preset.PresetStorage
import com.example.lightcalculator.viewmodel.CalculationViewModel

@Composable
fun LoadPresetScreen(
    navController: NavHostController,
    viewModel: CalculationViewModel
) {
    val context = LocalContext.current
    var presets by remember { mutableStateOf(emptyList<com.example.lightcalculator.data.preset.Preset>()) }

    LaunchedEffect(Unit) {
        presets = PresetStorage.loadPresets(context)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray)
        .padding(16.dp)) {
        Text(
            text = stringResource(R.string.load_preset_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp),
            color = Color.Black
        )

        if (presets.isEmpty()) {
            Text(text = stringResource(R.string.load_preset_empty))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(presets) { preset ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.setItems(preset.lights)
                                navController.navigate("result?fromPreset=true")
                            }
                    ) {
                        Text(
                            text = preset.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
