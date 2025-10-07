package id.reishandy.fueltracker.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.reishandy.fueltracker.data.fuel.Fuel
import id.reishandy.fueltracker.data.fuel.FuelRepository
import id.reishandy.fueltracker.data.vehicle.Vehicle
import id.reishandy.fueltracker.util.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FuelFormErrorState(
    var dateError: String? = null,
    var odometerError: String? = null,
    var tripError: String? = null,
    var fuelAddedError: String? = null,
    var fuelTypeError: String? = null,
    var pricePerLiterError: String? = null,
)

data class FuelFormState(
    val errorState: FuelFormErrorState = FuelFormErrorState(),
    val isProcessing: Boolean = false,
    val showSheet: Boolean = false,
    val isEdit: Boolean = false,
    val selectedFuel: Fuel? = null,
)

@HiltViewModel
class FuelFormViewModel @Inject constructor(
    val fuelRepository: FuelRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(FuelFormState())
    val uiState: StateFlow<FuelFormState> = _uiState.asStateFlow()

    var date by mutableStateOf(System.currentTimeMillis())
        private set

    var odometer by mutableStateOf("")
        private set

    var trip by mutableStateOf("")
        private set

    var fuelAdded by mutableStateOf("")
        private set

    var fuelType by mutableStateOf("")
        private set

    var pricePerLiter by mutableStateOf("")
        private set

    fun updateDate(newDate: Long) {
        date = newDate
    }

    fun updateOdometer(newOdometer: String) {
        odometer = newOdometer
    }

    fun updateTrip(newTrip: String) {
        trip = newTrip
    }

    fun updateFuelAdded(newFuelAdded: String) {
        fuelAdded = newFuelAdded
    }

    fun updateFuelType(newFuelType: String) {
        fuelType = newFuelType
    }

    fun updatePricePerLiter(newPricePerLiter: String) {
        pricePerLiter = newPricePerLiter
    }

    fun setupEdit(
        fuel: Fuel
    ) {
        date = fuel.date
        odometer = fuel.odometer.toString()
        trip = fuel.trip.toString()
        fuelAdded = fuel.fuelAdded.toString()
        fuelType = fuel.fuelType
        pricePerLiter = fuel.pricePerLiter.toString()
        _uiState.update {
            it.copy(
                errorState = FuelFormErrorState(),
                isEdit = true,
                selectedFuel = fuel,
            )
        }
    }

    fun clearEdit() {
        _uiState.update {
            it.copy(
                errorState = FuelFormErrorState(),
                isEdit = false,
                selectedFuel = null,
            )
        }
        resetForm()
    }

    fun resetForm() {
        date = System.currentTimeMillis()
        odometer = ""
        trip = ""
        fuelAdded = ""
        fuelType = ""
        pricePerLiter = ""
        _uiState.value = FuelFormState()
    }

    fun setProcessing(isProcessing: Boolean) {
        _uiState.update { it.copy(isProcessing = isProcessing) }
    }

    fun showSheet() {
        _uiState.update { it.copy(showSheet = true) }
    }

    fun hideSheet() {
        _uiState.update { it.copy(showSheet = false) }
    }

    fun validateForm(
        vehicle: Vehicle,
        previousOdometer: Int?
    ): Boolean {
        var isValid = true
        val errorState = FuelFormErrorState()

        if (odometer.isBlank()) {
            errorState.odometerError = "Odometer is required"
            isValid = false
        } else {
            val odometerValue = odometer.toIntOrNull()
            if (odometerValue == null || odometerValue < 0) {
                errorState.odometerError = "Odometer must be a positive number"
                isValid = false
            }

            if (previousOdometer != null && odometerValue != null && odometerValue < previousOdometer) {
                errorState.odometerError = "Odometer cannot be less than previous reading ($previousOdometer km)"
                isValid = false
            }
        }

        if (trip.isBlank()) {
            errorState.tripError = "Trip is required"
            isValid = false
        } else {
            val tripValue = trip.toIntOrNull()
            if (tripValue == null || tripValue < 0) {
                errorState.tripError = "Trip must be a positive number"
                isValid = false
            }
        }

        if (fuelAdded.isBlank()) {
            errorState.fuelAddedError = "Fuel added is required"
            isValid = false
        } else {
            val fuelAddedValue = fuelAdded.toDoubleOrNull()
            if (fuelAddedValue == null || fuelAddedValue <= 0) {
                errorState.fuelAddedError = "Fuel added must be a positive number"
                isValid = false
            }

            if (fuelAddedValue != null && fuelAddedValue > vehicle.maxFuelCapacity) {
                errorState.fuelAddedError = "Fuel added cannot exceed max capacity (${vehicle.maxFuelCapacity} L)"
                isValid = false
            }
        }

        if (fuelType.isBlank()) {
            errorState.fuelTypeError = "Fuel type is required"
            isValid = false
        }

        if (pricePerLiter.isBlank()) {
            errorState.pricePerLiterError = "Price per liter is required"
            isValid = false
        } else {
            val pricePerLiterValue = pricePerLiter.toDoubleOrNull()
            if (pricePerLiterValue == null || pricePerLiterValue <= 0) {
                errorState.pricePerLiterError = "Price per liter must be a positive number"
                isValid = false
            }
        }

        _uiState.update { it.copy(errorState = errorState) }
        return isValid
    }

    fun addFuel(
        context: Context,
        vehicle: Vehicle,
        previousOdometer: Int?,
        onSuccess: () -> Unit = { },
    ) {
        if (!validateForm(vehicle, previousOdometer)) return

        viewModelScope.launch {
            try {
                setProcessing(true)
                // TODO: Sync cloud?

                val newFuel = Fuel(
                    date = date,
                    odometer = odometer.toInt(),
                    trip = trip.toInt(),
                    fuelAdded = fuelAdded.toDouble(),
                    fuelType = fuelType,
                    pricePerLiter = pricePerLiter.toDouble(),
                    vehicleId = vehicle.id,
                    // This will be calculated in repository
                    totalCost = 0.0,
                    fuelEconomy = 0.0,
                    costPerKm = 0.0,
                    fuelRemaining = 0.0,
                )
                fuelRepository.insert(newFuel)

                                showToast(context, "Refuel recorded successfully")

                onSuccess()
            } catch (e: Exception) {
                showToast(context, "Error recording fuel: ${e.message}", true)
            } finally {
                setProcessing(false)
            }
        }
    }

    fun updateFuel(
        context: Context,
        vehicle: Vehicle,
        previousOdometer: Int?,
        fuel: Fuel,
        onSuccess: () -> Unit = { },
    ) {
        if (!validateForm(vehicle, previousOdometer)) return

        viewModelScope.launch {
            try {
                setProcessing(true)

                val updatedFuel = fuel.copy(
                    date = date,
                    odometer = odometer.toInt(),
                    trip = trip.toInt(),
                    fuelAdded = fuelAdded.toDouble(),
                    fuelType = fuelType,
                    pricePerLiter = pricePerLiter.toDouble(),
                )
                fuelRepository.update(updatedFuel)

                showToast(context, "Refuel edited successfully")

                onSuccess()
            } catch (e: Exception) {
                showToast(context, "Error updating refuel: ${e.message}", true)
            } finally {
                setProcessing(false)
            }
        }
    }

    fun calculateTripFromPreviousOdometer(previousOdometer: Int?) {
        if (previousOdometer == null) return
        val odometerValue = odometer.toIntOrNull()

        if (odometerValue == null || odometerValue < 0) {
            _uiState.update {
                it.copy(
                    errorState = it.errorState.copy(
                        odometerError = "Odometer must be a positive number"
                    )
                )
            }
            return
        }

        if (odometerValue < previousOdometer) {
            _uiState.update {
                it.copy(
                    errorState = it.errorState.copy(
                        odometerError = "Odometer cannot be less than previous reading ($previousOdometer km)"
                    )
                )
            }
            return
        }

        trip = (odometerValue - previousOdometer).toString()

        _uiState.update {
            it.copy(
                errorState = it.errorState.copy(
                    odometerError = null
                )
            )
        }

    }
}