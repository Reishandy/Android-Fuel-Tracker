package id.reishandy.fueltracker.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.reishandy.fueltracker.data.vehicle.Vehicle
import id.reishandy.fueltracker.data.vehicle.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class VehicleFormErrorState(
    var nameError: String? = null,
    var manufacturerError: String? = null,
    var modelError: String? = null,
    var yearError: String? = null,
    var maxFuelError: String? = null,
)

data class VehicleFormState(
    val errorState: VehicleFormErrorState = VehicleFormErrorState(),
    val showSheet: Boolean = false,
    val isProcessing: Boolean = false,
    val isEdit: Boolean = false,
    val selectedVehicle: Vehicle? = null,
)

@HiltViewModel
class VehicleFormViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(VehicleFormState())
    val uiState: StateFlow<VehicleFormState> = _uiState.asStateFlow()

    var name by mutableStateOf("")
        private set
    var manufacturer by mutableStateOf("")
        private set
    var model by mutableStateOf("")
        private set
    var year by mutableStateOf("")
        private set
    var maxFuel by mutableStateOf("")
        private set

    fun updateName(newName: String) {
        name = newName.trim()
    }

    fun updateManufacturer(newManufacturer: String) {
        manufacturer = newManufacturer.trim()
    }

    fun updateModel(newModel: String) {
        model = newModel.trim()
    }

    fun updateYear(newYear: String) {
        year = newYear
    }

    fun updateMaxFuel(newMaxFuel: String) {
        maxFuel = newMaxFuel
    }

    fun setupEdit(
        vehicle: Vehicle,
    ) {
        name = vehicle.name
        manufacturer = vehicle.manufacturer
        model = vehicle.model
        year = vehicle.year.toString()
        maxFuel = vehicle.maxFuelCapacity.toString()
        _uiState.update {
            it.copy(
                isEdit = true,
                selectedVehicle = vehicle,
            )
        }
    }

    fun clearEdit() {
        _uiState.update {
            it.copy(
                isEdit = false,
                selectedVehicle = null,
            )
        }
        resetForm()
    }

    fun resetForm() {
        name = ""
        manufacturer = ""
        model = ""
        year = ""
        maxFuel = ""
        _uiState.value = VehicleFormState()
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

    fun validateForm(): Boolean {
        var isValid = true
        val errorState = VehicleFormErrorState()

        if (name.isBlank()) {
            errorState.nameError = "Name cannot be empty"
            isValid = false
        }

        if (manufacturer.isBlank()) {
            errorState.manufacturerError = "Manufacturer cannot be empty"
            isValid = false
        }

        if (model.isBlank()) {
            errorState.modelError = "Model cannot be empty"
            isValid = false
        }

        if (year.isBlank()) {
            errorState.yearError = "Year cannot be empty"
            isValid = false
        } else {
            val maxYear = LocalDate.now().year + 5 // Allow up to 5 years in the future
            val yearInt = year.toIntOrNull()
            if (yearInt == null || yearInt < 1886 || yearInt > maxYear) { // First car invented in 1886
                errorState.yearError = "Year must be a valid number between 1886 and $maxYear"
                isValid = false
            }
        }

        if (maxFuel.isBlank()) {
            errorState.maxFuelError = "Max fuel cannot be empty"
            isValid = false
        } else {
            val maxFuelFloat = maxFuel.toDoubleOrNull()
            if (maxFuelFloat == null || maxFuelFloat <= 0) {
                errorState.maxFuelError = "Max fuel must be a valid positive decimal number"
                isValid = false
            }
        }

        _uiState.update { it.copy(errorState = errorState) }
        return isValid
    }

    fun addVehicle(
        context: Context,
        onSuccess: () -> Unit = { },
    ) {
        if (!validateForm()) return

        viewModelScope.launch {
            try {
                setProcessing(true)
                // TODO: Sync cloud?

                val newVehicle = Vehicle(
                    name = name,
                    manufacturer = manufacturer,
                    model = model,
                    year = year.toInt(),
                    maxFuelCapacity = maxFuel.toDouble(),
                )
                vehicleRepository.insert(newVehicle)

                resetForm()
                showToast(context, "Vehicle added successfully")

                onSuccess()
            } catch (e: Exception) {
                showToast(context, "Error adding vehicle: ${e.message}", true)
            } finally {
                setProcessing(false)
            }
        }
    }

    fun updateVehicle(
        context: Context,
        vehicle: Vehicle,
        onSuccess: () -> Unit = { },
    ) {
        if (!validateForm()) return

        viewModelScope.launch {
            try {
                setProcessing(true)

                val updatedVehicle = vehicle.copy(
                    name = name,
                    manufacturer = manufacturer,
                    model = model,
                    year = year.toInt(),
                    maxFuelCapacity = maxFuel.toDouble(),
                )
                vehicleRepository.update(updatedVehicle)

                clearEdit()
                showToast(context, "Vehicle edited successfully")

                onSuccess()
            } catch (e: Exception) {
                showToast(context, "Error editing vehicle: ${e.message}", true)
            } finally {
                setProcessing(false)
            }
        }
    }
}