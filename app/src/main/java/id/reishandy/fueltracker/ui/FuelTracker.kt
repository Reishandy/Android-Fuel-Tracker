package id.reishandy.fueltracker.ui

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.reishandy.fueltracker.model.VehicleFormViewModelFactory
import id.reishandy.fueltracker.model.view.VehicleFormViewModel
import id.reishandy.fueltracker.ui.component.VehicleFormBottomSheet
import id.reishandy.fueltracker.ui.view.Home
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class FuelTrackerNav {
    HOME,
    ADD_VEHICLE,
    VEHICLE_DETAIL,
    ADD_REFUELING,
    SETTINGS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelTracker() {
    val navController: NavHostController = rememberNavController()
    val context: Context = LocalContext.current
    val scope: CoroutineScope = rememberCoroutineScope()

    val vehicleFormViewModel: VehicleFormViewModel =
        viewModel(factory = VehicleFormViewModelFactory(context))

    val vehicleFormUiState by vehicleFormViewModel.uiState.collectAsState()

    val sheetState = androidx.compose.material3.rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = FuelTrackerNav.HOME.name,
            modifier = Modifier.statusBarsPadding()
        ) {
            composable(route = FuelTrackerNav.HOME.name) {
                Home(
                    onAddVehicleClick = {
                        vehicleFormViewModel.showSheet()
                    }
                )
            }
        }

        VehicleFormBottomSheet(
            onDismissRequest = {
                vehicleFormViewModel.hideSheet()
            },
            onCloseButtonClick = {
                scope.launch {
                    sheetState.hide()
                    vehicleFormViewModel.hideSheet()
                }
            },
            onSaveButtonClick = {
                vehicleFormViewModel.addVehicle(
                    context = context,
                    onSuccess = {
                        scope.launch {
                            sheetState.hide()
                            vehicleFormViewModel.hideSheet()
                        }
                    }
                )
            },
            sheetState = sheetState,
            isProcessing = vehicleFormUiState.isProcessing,
            showSheet = vehicleFormUiState.showSheet,
            nameValue = vehicleFormViewModel.name,
            onNameValueChange = { vehicleFormViewModel.updateName(it) },
            nameError = vehicleFormUiState.errorState.nameError,
            manufacturerValue = vehicleFormViewModel.manufacturer,
            onManufacturerValueChange = { vehicleFormViewModel.updateManufacturer(it) },
            manufacturerError = vehicleFormUiState.errorState.manufacturerError,
            modelValue = vehicleFormViewModel.model,
            onModelValueChange = { vehicleFormViewModel.updateModel(it) },
            modelError = vehicleFormUiState.errorState.modelError,
            yearValue = vehicleFormViewModel.year,
            onYearValueChange = { vehicleFormViewModel.updateYear(it) },
            yearError = vehicleFormUiState.errorState.yearError,
            maxFuelValue = vehicleFormViewModel.maxFuel,
            onMaxFuelValueChange = { vehicleFormViewModel.updateMaxFuel(it) },
            maxFuelError = vehicleFormUiState.errorState.maxFuelError
        )
    }
}

// TODO: Features and stuff
//  - VehicleItem onCLick animation and hold for edit or delete
//  - CRUD Vehicle
//  - CRUD Refueling
//  - CRUD Maintenance
//  - Statistics (charts?)
//  - Backup and restore (google drive?)
//  - Google login