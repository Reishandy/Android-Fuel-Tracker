package id.reishandy.fueltracker.ui

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.reishandy.fueltracker.model.AuthViewModel
import id.reishandy.fueltracker.model.DeleteType
import id.reishandy.fueltracker.model.DeleteViewModel
import id.reishandy.fueltracker.model.FuelFormViewModel
import id.reishandy.fueltracker.model.FuelViewModel
import id.reishandy.fueltracker.model.SettingViewModel
import id.reishandy.fueltracker.model.VehicleFormViewModel
import id.reishandy.fueltracker.model.VehicleViewModel
import id.reishandy.fueltracker.ui.component.DeleteBottomSheet
import id.reishandy.fueltracker.ui.component.FuelFormBottomSheet
import id.reishandy.fueltracker.ui.component.VehicleFormBottomSheet
import id.reishandy.fueltracker.ui.view.Detail
import id.reishandy.fueltracker.ui.view.Home
import id.reishandy.fueltracker.ui.view.Setting
import id.reishandy.fueltracker.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class FuelTrackerNav {
    HOME, VEHICLE_DETAIL, SETTING
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
    val fuelFormViewModel: FuelFormViewModel = hiltViewModel()
    val deleteViewModel: DeleteViewModel = hiltViewModel()
    val settingViewModel: SettingViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()

    val vehicleUiState by vehicleViewModel.uiState.collectAsState()
    val vehicleFormUiState by vehicleFormViewModel.uiState.collectAsState()
    val fuelUiState by fuelViewModel.uiState.collectAsState()
    val fuelFormUiState by fuelFormViewModel.uiState.collectAsState()
    val deleteUiState by deleteViewModel.uiState.collectAsState()
    val settingUiState by settingViewModel.uiState.collectAsState()
    val authUiState by authViewModel.uiState.collectAsState()

    val vehicleSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val fuelSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val deleteSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = FuelTrackerNav.HOME.name,
            modifier = Modifier.statusBarsPadding()
        ) {
            composable(route = FuelTrackerNav.HOME.name) {
                var shouldExit by remember { mutableStateOf(false) }

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
                        deleteViewModel.setIsOnDetails(false)
                        deleteViewModel.updateSelectedVehicle(vehicle)
                        deleteViewModel.showSheet()
                    },
                    onVehicleClick = { vehicleWithStats ->
                        shouldExit = true
                        scope.launch {
                            kotlinx.coroutines.delay(300)
                            fuelFormViewModel.clearEdit()
                            vehicleViewModel.updateSelectedVehicleWithStats(vehicleWithStats)
                            fuelViewModel.populateFuels(vehicleWithStats.vehicle.id)
                            navController.navigate(FuelTrackerNav.VEHICLE_DETAIL.name)
                        }
                    },
                    onProfileClick = {
                        shouldExit = true
                        scope.launch {
                            kotlinx.coroutines.delay(300)
                            navController.navigate(FuelTrackerNav.SETTING.name)
                        }
                    },
                    name = authUiState.name,
                    profilePhotoUrl = authUiState.photoUrl,
                    shouldExit = shouldExit,
                )
            }

            composable(route = FuelTrackerNav.VEHICLE_DETAIL.name) {
                if (vehicleUiState.selectedVehicleWithStats == null) {
                    navController.popBackStack()
                    showToast(context, "Selected vehicle not found, should not be possible", true)
                }
                var shouldExit by remember { mutableStateOf(false) }

                Detail(
                    onBackClick = {
                        shouldExit = true
                        scope.launch {
                            kotlinx.coroutines.delay(300)
                            navController.popBackStack()
                        }
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
                        fuelFormViewModel.showSheet()
                    },
                    onFuelEditClick = { fuel ->
                        fuelFormViewModel.setupEdit(fuel)
                        fuelFormViewModel.showSheet()
                    },
                    onFuelDeleteClick = { fuel ->
                        deleteViewModel.updateSelectedFuel(fuel)
                        deleteViewModel.setDeleteType(DeleteType.FUEL)
                        deleteViewModel.showSheet()
                    },
                    shouldExit = shouldExit
                )
            }

            composable(route = FuelTrackerNav.SETTING.name) {
                var shouldExit by remember { mutableStateOf(false) }

                Setting(
                    onBackClick = {
                        shouldExit = true
                        scope.launch {
                            kotlinx.coroutines.delay(300)
                            navController.popBackStack()
                        }
                    },
                    onLoginClick = {
                        authViewModel.signInWithGoogle(
                            context = context, request = authViewModel.createSignInRequest(
                                authViewModel.createGoogleSignInOption(false)
                            )
                        )
                    },
                    onLogoutClick = {
                        authViewModel.signOut(context)
                        navController.popBackStack(FuelTrackerNav.HOME.name, false)
                    },
                    locales = settingUiState.locales,
                    selectedLocale = settingUiState.selectedLocale,
                    onLocaleSelected = { locale ->
                        settingViewModel.updateSelectedLocale(locale, context as? Activity)
                    },
                    name = authUiState.name,
                    email = authUiState.email,
                    profilePhotoUrl = authUiState.photoUrl,
                    isProcessing = authUiState.isProcessing,
                    shouldExit = shouldExit,
                )
            }
        }

        FuelFormBottomSheet(
            onDismissRequest = {
                fuelFormViewModel.hideSheet()
                if (fuelFormUiState.isEdit) {
                    fuelFormViewModel.clearEdit()
                }
            },
            onCloseButtonClick = {
                scope.launch {
                    fuelSheetState.hide()
                    fuelFormViewModel.hideSheet()
                    if (fuelFormUiState.isEdit) {
                        fuelFormViewModel.clearEdit()
                    }
                }
            },
            onSaveButtonClick = {
                if (fuelFormUiState.isEdit) {
                    fuelFormViewModel.updateFuel(
                        context = context,
                        vehicle = vehicleUiState.selectedVehicleWithStats!!.vehicle,
                        fuel = fuelFormUiState.selectedFuel!!,
                        previousOdometer = fuelUiState.fuels.filter { fuel ->
                            fuel.date < fuelFormUiState.selectedFuel!!.date || (fuel.date == fuelFormUiState.selectedFuel!!.date && fuel.createdAt < fuelFormUiState.selectedFuel!!.createdAt)
                        }.maxWithOrNull(compareBy({ it.date }, { it.createdAt }))?.odometer,
                        onSuccess = {
                            scope.launch {
                                fuelViewModel.populateFuels(vehicleUiState.selectedVehicleWithStats!!.vehicle.id)
                                vehicleViewModel.updateSelectedVehicleAfterEdit()
                                fuelSheetState.hide()
                                fuelFormViewModel.clearEdit()
                                fuelFormViewModel.hideSheet()
                            }
                        })
                } else {
                    fuelFormViewModel.addFuel(
                        context = context,
                        vehicle = vehicleUiState.selectedVehicleWithStats!!.vehicle,
                        previousOdometer = fuelUiState.fuels.maxWithOrNull(
                            compareBy(
                                { it.date },
                                { it.createdAt })
                        )?.odometer,
                        onSuccess = {
                            scope.launch {
                                fuelViewModel.populateFuels(vehicleUiState.selectedVehicleWithStats!!.vehicle.id)
                                vehicleViewModel.updateSelectedVehicleAfterEdit()
                                fuelSheetState.hide()
                                fuelFormViewModel.resetForm()
                                fuelFormViewModel.hideSheet()
                            }
                        })
                }
            },
            sheetState = fuelSheetState,
            isProcessing = fuelFormUiState.isProcessing,
            isEdit = fuelFormUiState.isEdit,
            showSheet = fuelFormUiState.showSheet,
            dateValue = fuelFormViewModel.date,
            onDateValueChange = { fuelFormViewModel.updateDate(it) },
            dateError = fuelFormUiState.errorState.dateError,
            odometerValue = fuelFormViewModel.odometer,
            onOdometerValueChange = { fuelFormViewModel.updateOdometer(it) },
            odometerError = fuelFormUiState.errorState.odometerError,
            tripValue = fuelFormViewModel.trip,
            onTripValueChange = { fuelFormViewModel.updateTrip(it) },
            tripError = fuelFormUiState.errorState.tripError,
            fuelAddedValue = fuelFormViewModel.fuelAdded,
            onFuelAddedValueChange = { fuelFormViewModel.updateFuelAdded(it) },
            fuelAddedError = fuelFormUiState.errorState.fuelAddedError,
            fuelTypeValue = fuelFormViewModel.fuelType,
            onFuelTypeValueChange = { fuelFormViewModel.updateFuelType(it) },
            fuelTypeError = fuelFormUiState.errorState.fuelTypeError,
            pricePerLiterValue = fuelFormViewModel.pricePerLiter,
            onPricePerLiterValueChange = { fuelFormViewModel.updatePricePerLiter(it) },
            pricePerLiterError = fuelFormUiState.errorState.pricePerLiterError,
            canCalculateTrip = fuelFormViewModel.odometer.isNotEmpty() && fuelUiState.fuels.isNotEmpty() &&
                    (if (fuelFormUiState.isEdit)
                        fuelUiState.fuels.any { fuel ->
                            fuel.date < fuelFormUiState.selectedFuel!!.date ||
                                    (fuel.date == fuelFormUiState.selectedFuel!!.date && fuel.createdAt < fuelFormUiState.selectedFuel!!.createdAt)
                        }
                    else true),
            onCanCalculateTripClick = {
                val previousOdometer = if (fuelFormUiState.isEdit) {
                    fuelUiState.fuels.filter { fuel ->
                        fuel.date < fuelFormUiState.selectedFuel!!.date || (fuel.date == fuelFormUiState.selectedFuel!!.date && fuel.createdAt < fuelFormUiState.selectedFuel!!.createdAt)
                    }.maxWithOrNull(compareBy({ it.date }, { it.createdAt }))?.odometer
                } else {
                    fuelUiState.fuels.maxWithOrNull(
                        compareBy(
                            { it.date },
                            { it.createdAt })
                    )?.odometer
                }
                fuelFormViewModel.calculateTripFromPreviousOdometer(previousOdometer)
            })


        VehicleFormBottomSheet(
            onDismissRequest = {
                vehicleFormViewModel.hideSheet()
                if (vehicleFormUiState.isEdit) vehicleFormViewModel.clearEdit()
            },
            onCloseButtonClick = {
                scope.launch {
                    vehicleSheetState.hide()
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
                                vehicleViewModel.updateSelectedVehicleAfterEdit()
                                vehicleSheetState.hide()
                                vehicleFormViewModel.clearEdit()
                                vehicleFormViewModel.hideSheet()
                            }
                        })
                } else {
                    vehicleFormViewModel.addVehicle(context = context, onSuccess = {
                        scope.launch {
                            vehicleSheetState.hide()
                            vehicleFormViewModel.resetForm()
                            vehicleFormViewModel.hideSheet()
                        }
                    })
                }
            },
            sheetState = vehicleSheetState,
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
                if (deleteUiState.deleteType == DeleteType.VEHICLE) {
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
                        })
                } else {
                    deleteViewModel.deleteFuel(
                        context = context,
                        fuel = deleteUiState.selectedFuel!!,
                        onSuccess = {
                            scope.launch {
                                vehicleViewModel.updateSelectedVehicleAfterEdit()
                                fuelViewModel.removeFromList(deleteUiState.selectedFuel!!)
                                deleteSheetState.hide()
                                deleteViewModel.clear()
                                deleteViewModel.hideSheet()
                            }
                        })
                }
            },
            sheetState = deleteSheetState,
            isProcessing = deleteUiState.isProcessing,
            showSheet = deleteUiState.showSheet,
            name = deleteUiState.name
        )
    }
}