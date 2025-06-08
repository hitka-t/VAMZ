package com.example.lightcalculator.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lightcalculator.R

@Composable
fun HomeScreen(navController: NavController) {
    var showInfoDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(WindowInsets.statusBars.asPaddingValues()) // ochrana pred status barom
    ) {
        // Info button v pravom hornom rohu
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = stringResource(R.string.info),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .clickable { showInfoDialog = true },
            tint = Color.Black
        )

        // Tlacidla v strede
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tlacidlo NOVY
            Button(
                onClick = {
                    navController.navigate("new")
                          },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.new_button),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            // Tlacidlo LOAD PRESET
            Button(
                onClick = {
                    navController.navigate("load")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.load_preset_button),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }

        // Info dialog
        if (showInfoDialog) {
            AlertDialog(
                onDismissRequest = { showInfoDialog = false },
                title = { Text(stringResource(R.string.HOME_info_title)) },
                text = { Text(stringResource(R.string.HOME_info_title_des)) },
                confirmButton = {
                    TextButton(onClick = { showInfoDialog = false }) {
                        Text(stringResource(R.string.ok))
                    }
                }
            )
        }
    }
}
