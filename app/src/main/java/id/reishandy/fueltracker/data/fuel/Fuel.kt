package id.reishandy.fueltracker.data.fuel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import id.reishandy.fueltracker.data.vehicle.Vehicle

@Entity(
    tableName = "fuels",
    foreignKeys = [
        ForeignKey(
            entity = Vehicle::class,
            parentColumns = ["id"],
            childColumns = ["vehicle_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["vehicle_id"]),
        Index(value = ["date"])
    ]
)
data class Fuel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "vehicle_id")
    val vehicleId: Long,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "odometer")
    val odometer: Int,

    @ColumnInfo(name = "trip")
    val trip: Int,

    @ColumnInfo(name = "fuel_added")
    val fuelAdded: Double,

    @ColumnInfo(name = "fuel_type")
    val fuelType: String,

    @ColumnInfo(name = "price_per_liter")
    val pricePerLiter: Double,

    @ColumnInfo(name = "total_cost")
    var totalCost: Double,

    @ColumnInfo(name = "fuel_economy")
    var fuelEconomy: Double,

    @ColumnInfo(name = "cost_per_km")
    var costPerKm: Double,

    @ColumnInfo(name = "fuel_remaining")
    var fuelRemaining: Double,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)