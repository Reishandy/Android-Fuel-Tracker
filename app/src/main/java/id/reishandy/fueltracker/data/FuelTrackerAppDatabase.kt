package id.reishandy.fueltracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import id.reishandy.fueltracker.data.fuel.Fuel
import id.reishandy.fueltracker.data.fuel.FuelDao
import id.reishandy.fueltracker.data.vehicle.Vehicle
import id.reishandy.fueltracker.data.vehicle.VehicleDao

@Database(
    entities = [Vehicle::class, Fuel::class],
    version = 4, // INFO: Do not forget to create local and cloud migration when changing version
    exportSchema = false
)
abstract class FuelTrackerAppDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
    abstract fun fuelDao(): FuelDao
}