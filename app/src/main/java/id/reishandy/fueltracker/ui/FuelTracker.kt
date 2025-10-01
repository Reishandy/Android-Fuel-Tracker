package id.reishandy.fueltracker.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.reishandy.fueltracker.ui.view.Home

enum class FuelTrackerNav {
    HOME,
    ADD_VEHICLE,
    VEHICLE_DETAIL,
    ADD_REFUELING,
    SETTINGS
}

@Composable
fun FuelTracker() {
    val navController: NavHostController = rememberNavController()

    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = FuelTrackerNav.HOME.name,
            modifier = Modifier.statusBarsPadding()
        ) {
            composable(route = FuelTrackerNav.HOME.name) {
                Home()
            }
        }
    }
}

// TODO: Features and stuff
//  - CRUD Vehicle
//  - CRUD Refueling
//  - CRUD Maintenance
//  - Statistics (charts?)
//  - Backup and restore (google drive?)
//  - Google login