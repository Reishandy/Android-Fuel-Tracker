package id.reishandy.fueltracker.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.reishandy.fueltracker.data.fuel.Fuel
import id.reishandy.fueltracker.data.fuel.FuelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    // Fuel added validation maximum is vehicle max tank capacity
}