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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.reishandy.fueltracker.model.DeleteViewModel
import id.reishandy.fueltracker.model.VehicleFormViewModel
import id.reishandy.fueltracker.model.VehicleViewModel
import id.reishandy.fueltracker.ui.component.DeleteBottomSheet
import id.reishandy.fueltracker.ui.component.VehicleFormBottomSheet
import id.reishandy.fueltracker.ui.view.Home
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class FuelTrackerNav {
    HOME,
    VEHICLE_DETAIL,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelTracker() {
    val navController: NavHostController = rememberNavController()
    val context: Context = LocalContext.current
    val scope: CoroutineScope = rememberCoroutineScope()

    val vehicleViewModel: VehicleViewModel = hiltViewModel()
    val vehicleFormViewModel: VehicleFormViewModel = hiltViewModel()
    val deleteViewModel: DeleteViewModel = hiltViewModel()

    val vehicleUiState by vehicleViewModel.uiState.collectAsState()
    val vehicleFormUiState by vehicleFormViewModel.uiState.collectAsState()
    val deleteUiState by deleteViewModel.uiState.collectAsState()

    val sheetState = androidx.compose.material3.rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val deleteSheetState = androidx.compose.material3.rememberModalBottomSheetState(
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
                    vehiclesWithStats = vehicleUiState.vehiclesWithStats,
                    onAddVehicleClick = {
                        vehicleFormViewModel.showSheet()
                    },
                    onEditVehicleClick = { vehicle ->
                        vehicleFormViewModel.setupEdit(vehicle)
                        vehicleFormViewModel.showSheet()
                    },
                    onDeleteVehicleClick = { vehicle ->
                        deleteViewModel.updateSelectedVehicle(vehicle)
                        deleteViewModel.showSheet()
                    },
                    onVehicleClick = { vehicleWithStats ->
                        /* TODO: Detail view model pass data */
                        navController.navigate(FuelTrackerNav.VEHICLE_DETAIL.name)
                    }
                )
            }

            composable(route = FuelTrackerNav.VEHICLE_DETAIL.name) {
                /* TODO: Vehicle Detail Screen */
            }
        }

        VehicleFormBottomSheet(
            onDismissRequest = {
                vehicleFormViewModel.hideSheet()
                if (vehicleFormUiState.isEdit) vehicleFormViewModel.clearEdit()
            },
            onCloseButtonClick = {
                scope.launch {
                    sheetState.hide()
                    vehicleFormViewModel.hideSheet()
                    if (vehicleFormUiState.isEdit) vehicleFormViewModel.clearEdit()
                }
            },
            onSaveButtonClick = {
                if (vehicleFormUiState.isEdit) {
                    vehicleFormViewModel.updateVehicle(
                        context = context,
                        vehicle = vehicleFormUiState.selectedVehicle!!,
                        onSuccess = {
                            scope.launch {
                                sheetState.hide()
                                vehicleFormViewModel.hideSheet()
                            }
                        }
                    )
                } else {
                    vehicleFormViewModel.addVehicle(
                        context = context,
                        onSuccess = {
                            scope.launch {
                                sheetState.hide()
                                vehicleFormViewModel.hideSheet()
                            }
                        }
                    )
                }
            },
            sheetState = sheetState,
            isProcessing = vehicleFormUiState.isProcessing,
            isEdit = vehicleFormUiState.isEdit,
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

        DeleteBottomSheet(
            onDismissRequest = {
                deleteViewModel.hideSheet()
            },
            onCloseButtonClick = {
                scope.launch {
                    deleteSheetState.hide()
                    deleteViewModel.hideSheet()
                }
            },
            onDeleteClick = {
                deleteViewModel.deleteVehicle(
                    context = context,
                    vehicle = deleteUiState.selectedVehicle!!,
                    onSuccess = {
                        scope.launch {
                            deleteSheetState.hide()
                            deleteViewModel.hideSheet()
                        }
                    }
                )
            },
            sheetState = deleteSheetState,
            isProcessing = deleteUiState.isProcessing,
            showSheet = deleteUiState.showSheet,
            name = deleteUiState.name
        )
    }
}

// TODO: Features and stuff
//  - CRUD Refueling
//  - Statistics (charts?)
//  - Google login
//  - Backup and restore (google drive?)
//  - Sync every CRUD or db update