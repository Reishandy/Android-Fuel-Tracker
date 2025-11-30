package id.reishandy.fueltracker.data.fuel

import id.reishandy.fueltracker.data.vehicle.VehicleDao
import javax.inject.Inject

class FuelRepository @Inject constructor(
    private val vehicleDao: VehicleDao,
    private val fuelDao: FuelDao
) {
    suspend fun insert(fuel: Fuel): Long {
        return fuelDao.insert(calculateDynamicFields(fuel))
    }

    suspend fun delete(fuel: Fuel) {
        fuelDao.delete(fuel)
    }

    suspend fun update(fuel: Fuel) {
        fuelDao.update(calculateDynamicFields(fuel))
    }

    fun getAll() = fuelDao.getAll()

    suspend fun getById(id: String) = fuelDao.getById(id)

    fun getByVehicleId(vehicleId: String) = fuelDao.getByVehicleId(vehicleId)

    private suspend fun calculateDynamicFields(fuel: Fuel): Fuel {
        val vehicle = vehicleDao.getById(fuel.vehicleId)
            ?: throw IllegalArgumentException("Vehicle with id ${fuel.vehicleId} not found")

        val totalCost = fuel.fuelAdded * fuel.pricePerLiter

        // Fetch previous entry for this vehicle (by date and created_at ordering)
        val previous = fuelDao.getPreviousEntry(fuel.vehicleId, fuel.date, fuel.createdAt)

        // Use previous entry's fuelAdded to compute fuel economy per user's request.
        // If there's no previous entry or previous.fuelAdded is zero, set economy to 0.0
        val fuelEconomy = if (fuel.trip > 0 && previous != null && previous.fuelAdded > 0.0) {
            fuel.trip / previous.fuelAdded
        } else {
            0.0
        }

        val costPerKm = if (fuel.trip > 0) totalCost / fuel.trip else 0.0
        val fuelRemaining = vehicle.maxFuelCapacity - fuel.fuelAdded

        return fuel.copy(
            totalCost = totalCost,
            fuelEconomy = fuelEconomy,
            costPerKm = costPerKm,
            fuelRemaining = fuelRemaining
        )
    }
}