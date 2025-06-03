package com.example.lightcalculator.ui.screen

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lightcalculator.R

@Composable
fun StartScreen(navController: NavController) {
    // Premenna, ktora urcuje, ci sa ma zobrazit info dialog
    var showInfoDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray) // Nastavi svetlosive pozadie obrazovky
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        // Info ikona v pravom hornom rohu
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = stringResource(R.string.info),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(25.dp)
                .clickable {
                    // Po kliknuti zobrazime info dialog
                    showInfoDialog = true
                },
            tint = Color.Black
        )

        // Gombik (obrazok) umiestneny v strede
        Image(
            painter = painterResource(id = R.drawable.play_button1), // Obrazok tlacidla z drawable
            contentDescription = "Play",
            modifier = Modifier
                .align(Alignment.Center)
                .size(240.dp)
                .clickable {
                    // Po kliknuti prepneme na dalsiu obrazovku (napr. home)
                    navController.navigate("home")
                }
        )

        // Ak je showInfoDialog true, zobrazime AlertDialog
        if (showInfoDialog) {
            AlertDialog(
                onDismissRequest = { showInfoDialog = false },
                title = { Text(text = stringResource(R.string.START_info_title)) },
                text = { Text(text = stringResource(R.string.START_info_title_des)) },
                confirmButton = {
                    TextButton(onClick = { showInfoDialog = false }) {
                        Text(stringResource(R.string.ok))
                    }
                }
            )
        }
    }
}
