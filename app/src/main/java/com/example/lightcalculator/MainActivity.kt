package com.example.lightcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.rememberNavController
import com.example.lightcalculator.screens.HomeScreen
import com.example.lightcalculator.screens.StartScreen
import com.example.lightcalculator.screens.StartScreen // ak bude dalsia obrazovka
import com.example.lightcalculator.ui.theme.LightCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Nastavime Compose obsah
        setContent {
            // Pouzijeme vlastnu temu definovanu v /ui/theme
            LightCalculatorTheme {
                // Povrch appky (pozadie)
                Surface(color = MaterialTheme.colorScheme.background) {
                    // Vytvorime navController na spravu navigacie
                    val navController = rememberNavController()
                    // Zavolame navigacnu funkciu
                    AppNavigation(navController)
                }
            }
        }
    }
}

// Navigacia medzi obrazovkami
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "start" // StartScreen je prva obrazovka
    ) {
        // definujem route pre StartScreen
        composable("start") {
            StartScreen(navController)
        }
        // Ak mas dalsiu obrazovku, napr. Home
        composable("home") {
            HomeScreen(navController)
        }
    }
}
