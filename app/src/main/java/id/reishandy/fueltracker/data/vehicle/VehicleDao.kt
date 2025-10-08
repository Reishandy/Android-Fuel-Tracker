package id.reishandy.fueltracker.data.vehicle

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(vehicle: Vehicle): Long

    @Delete
    suspend fun delete(vehicle: Vehicle)

    @Update
    suspend fun update(vehicle: Vehicle)

    @Query("SELECT * FROM vehicles ORDER BY created_at DESC")
    fun getAll(): Flow<List<Vehicle>>

    @Query("SELECT * FROM vehicles WHERE id = :id")
    suspend fun getById(id: Long): Vehicle?

    /**
     * Retrieves all vehicles with calculated statistics based on their fuel records.
     *
     * The statistics calculated for each vehicle include:
     * - latest_odometer: The highest odometer reading from all fuel records
     * - average_fuel_economy: Average of fuel economy across all refuels
     * - total_fuel_added: Sum of all fuel added to the vehicle
     * - total_spent: Total money spent on fuel for this vehicle
     * - refuel_count: Number of times the vehicle has been refueled
     * - refuel_per_month: Average number of refuels per month since the first recorded refuel
     *   (calculated using current date minus earliest fuel record date, converted to months)
     * - avg_liter_refueled: Average amount of fuel added per refueling
     * - avg_spent_per_refuel: Average amount of money spent per refueling
     *
     * COALESCE is used to provide default values (0) when no fuel records exist.
     *
     * @return A Flow emitting a list of vehicles with their calculated statistics
     */
    @Transaction
    @Query(
        """
    SELECT v.*,
    COALESCE((SELECT MAX(f.odometer) FROM fuels f WHERE f.vehicle_id = v.id), 0.0) AS latest_odometer,
    COALESCE((SELECT AVG(f.fuel_economy) FROM fuels f WHERE f.vehicle_id = v.id), 0.0) AS average_fuel_economy,
    COALESCE((SELECT SUM(f.fuel_added) FROM fuels f WHERE f.vehicle_id = v.id), 0.0) AS total_fuel_added,
    COALESCE((SELECT SUM(f.total_cost) FROM fuels f WHERE f.vehicle_id = v.id), 0.0) AS total_spent,
    COALESCE((SELECT COUNT(*) FROM fuels f WHERE f.vehicle_id = v.id), 0) AS refuel_count,
    COALESCE(
        CASE
            WHEN (SELECT COUNT(*) FROM fuels f WHERE f.vehicle_id = v.id) = 0 THEN 0.0
            ELSE
                (SELECT COUNT(*) FROM fuels f WHERE f.vehicle_id = v.id) /
                CASE 
                    WHEN ((julianday('now') - julianday(datetime(MIN(f2.date) / 1000, 'unixepoch'))) / 30.44) < 1.0 THEN 1.0
                    ELSE ((julianday('now') - julianday(datetime(MIN(f2.date) / 1000, 'unixepoch'))) / 30.44)
                END
        END, 0.0
    ) AS refuel_per_month,
    COALESCE((SELECT AVG(f.fuel_added) FROM fuels f WHERE f.vehicle_id = v.id), 0.0) AS avg_liter_refueled,
    COALESCE((SELECT AVG(f.total_cost) FROM fuels f WHERE f.vehicle_id = v.id), 0.0) AS avg_spent_per_refuel
    FROM vehicles v
    LEFT JOIN fuels f2 ON f2.vehicle_id = v.id
    GROUP BY v.id
    """
    )
    fun getAllWithStats(): Flow<List<VehicleWithStats>>

    /**
     * Retrieves a specific vehicle by ID with calculated statistics based on its fuel records.
     *
     * The statistics calculated for the vehicle include:
     * - latest_odometer: The highest odometer reading from all fuel records
     * - average_fuel_economy: Average of fuel economy across all refuels
     * - total_fuel_added: Sum of all fuel added to the vehicle
     * - total_spent: Total money spent on fuel for this vehicle
     * - refuel_count: Number of times the vehicle has been refueled
     * - refuel_per_month: Average number of refuels per month since the first recorded refuel
     *   (calculated using current date minus earliest fuel record date, converted to months)
     *   The constant 30.44 represents the average number of days in a month
     *   The calculation divides by 86400.0 to convert seconds to days
     * - avg_liter_refueled: Average amount of fuel added per refueling
     * - avg_spent_per_refuel: Average amount of money spent per refueling
     *
     * COALESCE is used to provide default values (0) when no fuel records exist.
     *
     * @param id The ID of the vehicle to retrieve with statistics
     * @return The vehicle with the specified ID and its calculated statistics, or null if not found
     */
    @Transaction
    @Query(
        """
    SELECT v.*,
    COALESCE((SELECT MAX(f.odometer) FROM fuels f WHERE f.vehicle_id = v.id), 0.0) AS latest_odometer,
    COALESCE((SELECT AVG(f.fuel_economy) FROM fuels f WHERE f.vehicle_id = v.id), 0.0) AS average_fuel_economy,
    COALESCE((SELECT SUM(f.fuel_added) FROM fuels f WHERE f.vehicle_id = v.id), 0.0) AS total_fuel_added,
    COALESCE((SELECT SUM(f.total_cost) FROM fuels f WHERE f.vehicle_id = v.id), 0.0) AS total_spent,
    COALESCE((SELECT COUNT(*) FROM fuels f WHERE f.vehicle_id = v.id), 0) AS refuel_count,
    COALESCE(
        CASE
            WHEN (SELECT COUNT(*) FROM fuels f WHERE f.vehicle_id = v.id) = 0 THEN 0.0
            ELSE
                (SELECT COUNT(*) FROM fuels f WHERE f.vehicle_id = v.id) /
                CASE 
                    WHEN ((julianday('now') - julianday(datetime(MIN(f2.date) / 1000, 'unixepoch'))) / 30.44) < 1.0 THEN 1.0
                    ELSE ((julianday('now') - julianday(datetime(MIN(f2.date) / 1000, 'unixepoch'))) / 30.44)
                END
        END, 0.0
    ) AS refuel_per_month,
    COALESCE((SELECT AVG(f.fuel_added) FROM fuels f WHERE f.vehicle_id = v.id), 0.0) AS avg_liter_refueled,
    COALESCE((SELECT AVG(f.total_cost) FROM fuels f WHERE f.vehicle_id = v.id), 0.0) AS avg_spent_per_refuel
    FROM vehicles v
    LEFT JOIN fuels f2 ON f2.vehicle_id = v.id
    WHERE v.id = :id
    GROUP BY v.id
    """
    )
    suspend fun getByIdWithStats(id: Long): VehicleWithStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vehicles: List<Vehicle>)

    @Query("DELETE FROM vehicles")
    suspend fun deleteAll()

    @Query("SELECT * FROM vehicles")
    suspend fun getAllSnapshot(): List<Vehicle>
}