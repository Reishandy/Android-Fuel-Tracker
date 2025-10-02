package id.reishandy.fueltracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import id.reishandy.fueltracker.data.vehicle.Vehicle
import id.reishandy.fueltracker.data.vehicle.VehicleDao

@Database(entities = [Vehicle::class], version = 1, exportSchema = false)
abstract class FuelTrackerAppDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
}