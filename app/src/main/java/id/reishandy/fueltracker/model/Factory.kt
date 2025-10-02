package id.reishandy.fueltracker.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.reishandy.fueltracker.model.view.VehicleFormViewModel

class VehicleFormViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return VehicleFormViewModel() as T
    }
}