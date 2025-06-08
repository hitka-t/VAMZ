package com.example.lightcalculator.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lightcalculator.R
import com.example.lightcalculator.data.AddedLight
import com.example.lightcalculator.data.preset.Preset
import com.example.lightcalculator.data.preset.PresetStorage
import com.example.lightcalculator.viewmodel.CalculationViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.math.ceil

private data class PhaseInfo(
    var totalPower: Int = 0,
    val lights: MutableMap<String, Int> = mutableMapOf()
)

private data class UniverseInfo(
    val contents: MutableList<UniverseLight> = mutableListOf()
)

private data class UniverseLight(
    val name: String,
    val startAddresses: MutableList<Int> = mutableListOf()
)

@Composable
fun CalculationResultScreen(sharedViewModel: CalculationViewModel, isFromPreset: Boolean) {
    val context = LocalContext.current
    val lights by sharedViewModel.items.collectAsState()

    var totalPower by remember { mutableStateOf<Int?>(null) }
    var universeCount by remember { mutableStateOf<Int?>(null) }
    var phaseData by remember { mutableStateOf<List<PhaseInfo>>(emptyList()) }
    var universeData by remember { mutableStateOf<List<UniverseInfo>>(emptyList()) }

    var showSaveDialog by remember { mutableStateOf(false) }
    var presetName by remember { mutableStateOf("") }

    LaunchedEffect(lights) {
        val lightUnits = mutableListOf<Triple<String, Int, Int>>()
        var total = 0
        var totalDmx = 0

        for (light in lights) {
            val (power, dmx) = getPowerAndDmx(light)
            val name = "${light.brand} ${light.model}"
            repeat(light.quantity) {
                lightUnits.add(Triple("$name (${light.dmxModeDes})", power, dmx))
                total += power
                totalDmx += dmx
            }
        }

        totalPower = total
        universeCount = ceil(totalDmx / 512.0).toInt()

        val phaseCount = ceil(total / 3680.0).toInt()
        val phases = List(phaseCount) { PhaseInfo() }

        for ((name, power, _) in lightUnits.sortedByDescending { it.second }) {
            val targetPhase = phases.minByOrNull { it.totalPower }!!
            targetPhase.totalPower += power
            targetPhase.lights[name] = targetPhase.lights.getOrDefault(name, 0) + 1
        }

        phaseData = phases

        val universes = mutableListOf<UniverseInfo>()
        var currentUniverse = UniverseInfo()
        var currentAddress = 1

        for ((name, _, dmx) in lightUnits) {
            if (currentAddress + dmx - 1 > 512) {
                universes.add(currentUniverse)
                currentUniverse = UniverseInfo()
                currentAddress = 1
            }

            val existing = currentUniverse.contents.find { it.name == name }
            if (existing != null) {
                existing.startAddresses.add(currentAddress)
            } else {
                currentUniverse.contents.add(UniverseLight(name, mutableListOf(currentAddress)))
            }

            currentAddress += dmx
        }

        if (currentUniverse.contents.isNotEmpty()) {
            universes.add(currentUniverse)
        }

        universeData = universes
    }

    val scrollState = rememberScrollState()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = stringResource(R.string.result_title), style = MaterialTheme.typography.titleLarge, color = Color.Black)

            if (totalPower == null || universeCount == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Text(
                    text = stringResource(R.string.result_total_power, totalPower.toString()),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Text(
                    text = stringResource(R.string.result_universe_count, universeCount!!),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.result_phase_count, phaseData.size),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                phaseData.forEachIndexed { index, phase ->
                    Text(
                        text = stringResource(R.string.result_phase_label, index + 1, phase.totalPower),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                    phase.lights.forEach { (name, count) ->
                        Text(
                            text = stringResource(R.string.result_phase_light_entry, count, name),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.result_universe_section_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                universeData.forEachIndexed { index, universe ->
                    Text(
                        text = stringResource(R.string.result_universe_label, index + 1),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                    universe.contents.forEach { light ->
                        val addresses = light.startAddresses.joinToString(", ")
                        Text(
                            text = stringResource(R.string.result_universe_light_entry, light.name, addresses),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        if (!isFromPreset) {
            Button(
                onClick = { showSaveDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.button_save))
            }
        }

        if (showSaveDialog) {
            AlertDialog(
                onDismissRequest = { showSaveDialog = false },
                title = { Text(stringResource(R.string.save_preset_title)) },
                text = {
                    OutlinedTextField(
                        value = presetName,
                        onValueChange = { presetName = it },
                        label = { Text(stringResource(R.string.save_preset_label)) },
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (presetName.isNotBlank()) {
                                PresetStorage.savePreset(context, Preset(presetName, lights))
                                Toast.makeText(context, context.getString(R.string.preset_saved), Toast.LENGTH_SHORT).show()
                                showSaveDialog = false
                                presetName = ""
                            }
                        }
                    ) {
                        Text(stringResource(R.string.button_save))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSaveDialog = false }) {
                        Text(stringResource(R.string.button_cancel))
                    }
                }
            )
        }
    }
}

private suspend fun getPowerAndDmx(light: AddedLight): Pair<Int, Int> {
    return suspendCancellableCoroutine { cont ->
        FirebaseFirestore.getInstance().collection("lights")
            .whereEqualTo("brand", light.brand)
            .whereEqualTo("model", light.model)
            .whereEqualTo("dmxmodeDes", light.dmxModeDes)
            .get()
            .addOnSuccessListener { result ->
                val doc = result.documents.firstOrNull()
                val power = doc?.getLong("power")?.toInt() ?: 0
                val dmx = doc?.getLong("dmxmode")?.toInt() ?: 0
                cont.resume(Pair(power, dmx))
            }
            .addOnFailureListener {
                cont.resume(Pair(0, 0))
            }
    }
}
