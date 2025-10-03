package id.reishandy.fueltracker.model

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

data class VehicleState(
    val vehicles: List<Vehicle> = emptyList(),
)

@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(VehicleState())
    val uiState: StateFlow<VehicleState> = _uiState.asStateFlow()

    init {
        try {

        } catch (e: Exception) {
            e.printStackTrace()
        }
        viewModelScope.launch {
            // TODO: Also get the avg and odometer
            vehicleRepository.getAllVehicles().collect { list ->
                _uiState.update { it.copy(vehicles = list) }
            }
        }
    }
}