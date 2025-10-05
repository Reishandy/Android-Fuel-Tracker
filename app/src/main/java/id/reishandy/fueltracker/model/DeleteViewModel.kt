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
import javax.inject.Inject

data class DeleteState(
    val showSheet: Boolean = false,
    val isProcessing: Boolean = false,
    val name: String = "",
    val selectedVehicle: Vehicle? = null,
)

@HiltViewModel
class DeleteViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DeleteState())
    val uiState: StateFlow<DeleteState> = _uiState.asStateFlow()

    var isOnDetails by mutableStateOf(false)
        private set

    fun setIsOnDetails(value: Boolean) {
        isOnDetails = value
    }

    fun showSheet() {
        _uiState.update { it.copy(showSheet = true) }
    }

    fun hideSheet() {
        _uiState.update { it.copy(showSheet = false) }
    }

    fun setProcessing(isProcessing: Boolean) {
        _uiState.update { it.copy(isProcessing = isProcessing) }
    }

    fun updateSelectedVehicle(vehicle: Vehicle?) {
        _uiState.update {
            it.copy(
                name = vehicle?.name ?: "N/A",
                selectedVehicle = vehicle
            )
        }
    }

    fun clear() {
        _uiState.update {
            it.copy(
                name = "",
                selectedVehicle = null
                // TODO: Fuel
            )
        }
    }

    fun deleteVehicle(
        context: Context,
        vehicle: Vehicle,
        onSuccess: () -> Unit = { },
    ) {
        viewModelScope.launch {
            try {
                setProcessing(true)

                vehicleRepository.delete(vehicle)

                showToast(context, "Vehicle deleted successfully")

                onSuccess()
            } catch (e: Exception) {
                showToast(context, "Error deleting vehicle: ${e.message}")
            } finally {
                setProcessing(false)
            }
        }
    }
}