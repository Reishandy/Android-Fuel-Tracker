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
import id.reishandy.fueltracker.model.FuelViewModel
import id.reishandy.fueltracker.model.VehicleFormViewModel
import id.reishandy.fueltracker.model.VehicleViewModel
import id.reishandy.fueltracker.model.showToast
import id.reishandy.fueltracker.ui.component.DeleteBottomSheet
import id.reishandy.fueltracker.ui.component.VehicleFormBottomSheet
import id.reishandy.fueltracker.ui.view.Detail
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
    val fuelViewModel: FuelViewModel = hiltViewModel()
    val deleteViewModel: DeleteViewModel = hiltViewModel()

    val vehicleUiState by vehicleViewModel.uiState.collectAsState()
    val vehicleFormUiState by vehicleFormViewModel.uiState.collectAsState()
    val fuelUiState by fuelViewModel.uiState.collectAsState()
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
                        vehicleViewModel.updateSelectedVehicleWithStats(vehicleWithStats)
                        fuelViewModel.populateFuels(vehicleWithStats.vehicle.id)
                        navController.navigate(FuelTrackerNav.VEHICLE_DETAIL.name)
                    }
                )
            }

            composable(route = FuelTrackerNav.VEHICLE_DETAIL.name) {
                if (vehicleUiState.selectedVehicleWithStats == null) {
                    navController.popBackStack()
                    showToast(context, "Selected vehicle not found, should not be possible", true)
                }

                Detail(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onEditClick = {
                        vehicleFormViewModel.setupEdit(vehicleUiState.selectedVehicleWithStats!!.vehicle)
                        vehicleFormViewModel.showSheet()
                    },
                    onDeleteClick = {
                        deleteViewModel.setIsOnDetails(true)
                        deleteViewModel.updateSelectedVehicle(vehicleUiState.selectedVehicleWithStats!!.vehicle)
                        deleteViewModel.showSheet()
                    },
                    vehicleWithStats = vehicleUiState.selectedVehicleWithStats!!,
                    fuels = fuelUiState.fuels,
                    onAddFuelClick = {
                        /* TODO: Implement add fuel  */
                    },
                    onFuelEditClick = { fuel ->
                        /* TODO: Implement edit fuel  */
                    },
                    onFuelDeleteClick = { fuel ->
                        /* TODO: Implement delete fuel  */
                    }
                )
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
                        onSuccess = { updatedVehicle ->
                            vehicleViewModel.updateSelectedVehicleAfterEdit(updatedVehicle)
                            scope.launch {
                                sheetState.hide()
                                vehicleFormViewModel.clearEdit()
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
                                vehicleFormViewModel.resetForm()
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
                            if (deleteViewModel.isOnDetails) {
                                deleteViewModel.setIsOnDetails(false)
                                navController.popBackStack()
                            }

                            deleteSheetState.hide()
                            deleteViewModel.clear()
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
//  - Locale setting
//  - Animations
//  - Custom splash screen
