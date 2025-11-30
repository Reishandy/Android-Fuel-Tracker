package id.reishandy.fueltracker.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("unused")
object FuelMigrations {
    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Load vehicle max capacity map
            val vehicleMaxCapacity = mutableMapOf<String, Double>()
            val vCursor = db.query("SELECT id, max_fuel_capacity FROM vehicles")
            while (vCursor.moveToNext()) {
                val id = vCursor.getString(0)
                val cap = if (!vCursor.isNull(1)) vCursor.getDouble(1) else 0.0
                vehicleMaxCapacity[id] = cap
            }
            vCursor.close()

            // Iterate fuels ordered by vehicle then date/created_at ascending so we can use previous row per vehicle
            val fuelCursor = db.query(
                "SELECT id, vehicle_id, date, created_at, trip, fuel_added, price_per_liter FROM fuels ORDER BY vehicle_id ASC, date ASC, created_at ASC"
            )

            var prevVehicleId: String? = null
            var prevFuelAdded = 0.0

            val stmt = db.compileStatement("UPDATE fuels SET total_cost = ?, fuel_economy = ?, cost_per_km = ?, fuel_remaining = ?, updated_at = ? WHERE id = ?")

            val now = System.currentTimeMillis()

            while (fuelCursor.moveToNext()) {
                val id = fuelCursor.getString(0)
                val vehicleId = fuelCursor.getString(1)
                val trip = if (!fuelCursor.isNull(4)) fuelCursor.getInt(4) else 0
                val fuelAdded = if (!fuelCursor.isNull(5)) fuelCursor.getDouble(5) else 0.0
                val pricePerLiter = if (!fuelCursor.isNull(6)) fuelCursor.getDouble(6) else 0.0

                if (prevVehicleId != vehicleId) {
                    // new vehicle sequence
                    prevVehicleId = vehicleId
                    prevFuelAdded = 0.0
                }

                val totalCost = fuelAdded * pricePerLiter

                val fuelEconomy = if (trip > 0) {
                    if (prevFuelAdded > 0.0) trip.toDouble() / prevFuelAdded else 0.0
                } else {
                    0.0
                }

                val costPerKm = if (trip > 0) totalCost / trip else 0.0
                val maxCap = vehicleMaxCapacity[vehicleId] ?: 0.0
                val fuelRemaining = maxCap - fuelAdded

                stmt.bindDouble(1, totalCost)
                stmt.bindDouble(2, fuelEconomy)
                stmt.bindDouble(3, costPerKm)
                stmt.bindDouble(4, fuelRemaining)
                stmt.bindLong(5, now)
                stmt.bindString(6, id)
                stmt.execute()

                // update prevFuelAdded for the next row of the same vehicle
                prevFuelAdded = fuelAdded
            }

            fuelCursor.close()
        }
    }
}
