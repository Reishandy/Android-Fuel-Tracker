package id.reishandy.fueltracker.data.vehicle

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class VehicleWithStats(
    @Embedded
    val vehicle: Vehicle,

    @ColumnInfo(name = "latest_odometer")
    val latestOdometer: Double = 0.0,

    @ColumnInfo(name = "average_fuel_economy")
    val averageFuelEconomy: Double = 0.0,

    @ColumnInfo(name = "total_fuel_added")
    val totalFuelAdded: Double = 0.0,

    @ColumnInfo(name = "total_spent")
    val totalSpent: Double = 0.0,

    @ColumnInfo(name = "refuel_count")
    val refuelCount: Int = 0,

    @ColumnInfo(name = "refuel_per_month")
    val refuelPerMonth: Double = 0.0,

    @ColumnInfo(name = "avg_liter_refueled")
    val avgLiterRefueled: Double = 0.0,

    @ColumnInfo(name = "avg_spent_per_refuel")
    val avgSpentPerRefuel: Double = 0.0
)
