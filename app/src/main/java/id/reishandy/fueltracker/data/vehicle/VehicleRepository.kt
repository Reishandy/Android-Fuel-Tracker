package id.reishandy.fueltracker.data.vehicle

import javax.inject.Inject

class VehicleRepository @Inject constructor(
    private val vehicleDao: VehicleDao
) {
    suspend fun insert(vehicle: Vehicle): Long {
        return vehicleDao.insert(vehicle)
    }

    suspend fun delete(vehicle: Vehicle) {
        vehicleDao.delete(vehicle)
    }

    suspend fun update(vehicle: Vehicle) {
        vehicleDao.update(vehicle)
    }

    fun getAllVehicles() = vehicleDao.getAllVehicles()

    suspend fun getVehicleById(id: Long) = vehicleDao.getVehicleById(id)
}