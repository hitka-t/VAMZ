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
import com.example.lightcalculator.data.Database
import com.example.lightcalculator.ui.screen.HomeScreen
import com.example.lightcalculator.ui.screen.NewCalculationScreen
import com.example.lightcalculator.ui.screen.StartScreen
import com.example.lightcalculator.ui.theme.LightCalculatorTheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.lightcalculator.data.Light
import com.example.lightcalculator.data.Lights

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pripravime databazu
        val db = Database.getDatabase(applicationContext)
        val dao = db.lightFixtureDao()

        // Vlozime vzorove data IBA ak databaza je prazdna
        lifecycleScope.launch {
            if (dao.getAllBrands().isEmpty()) {
                Lights.forEach { dao.insert(it) }
            }
        }

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
        composable("new") {
            NewCalculationScreen()
        }
    }
}
