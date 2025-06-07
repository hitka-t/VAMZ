package com.example.lightcalculator.ui.screen

import android.util.Log
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
import com.example.lightcalculator.data.populateSampleLightsIfEmpty
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@Composable
fun StartScreen(navController: NavController) {
    var showInfoDialog by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        populateSampleLightsIfEmpty()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = stringResource(R.string.info),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(25.dp)
                .clickable {
                    showInfoDialog = true
                },
            tint = Color.Black
        )

        Image(
            painter = painterResource(id = R.drawable.play_button1),
            contentDescription = "Play",
            modifier = Modifier
                .align(Alignment.Center)
                .size(240.dp)
                .clickable {
                    navController.navigate("home")
                }
        )

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