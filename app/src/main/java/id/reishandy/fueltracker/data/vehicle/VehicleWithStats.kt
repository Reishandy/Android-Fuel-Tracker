package id.reishandy.fueltracker.data.vehicle

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class VehicleWithStats(
    @Embedded
    val vehicle: Vehicle,

    @ColumnInfo(name = "latest_odometer")
    val latestOdometer: Double = 0.0,

    @ColumnInfo(name = "average_fuel_economy")
    val averageFuelEconomy: Double = 0.0
)