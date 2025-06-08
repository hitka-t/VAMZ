package com.example.lightcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lightcalculator.ui.screen.*
import com.example.lightcalculator.ui.theme.LightCalculatorTheme
import com.example.lightcalculator.viewmodel.CalculationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LightCalculatorTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    AppNavigation(navController)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val sharedViewModel: CalculationViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "start" //prva obrazovka
    ) {
        composable("start") {
            StartScreen(navController) // uvodna obrazovka
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("new") {
            NewCalculationScreen(navController, sharedViewModel)
        }
        composable(
            "result?fromPreset={fromPreset}",
            arguments = listOf(navArgument("fromPreset") {
                type = NavType.BoolType
                defaultValue = false
            })
        ) { backStackEntry ->
            val isFromPreset = backStackEntry.arguments?.getBoolean("fromPreset") ?: false
            CalculationResultScreen(sharedViewModel, isFromPreset)
        }
        composable("load") {
            LoadPresetScreen(navController, sharedViewModel)
        }
    }
}
