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

    @Transaction
    @Query(
        """
    SELECT v.*,
    (SELECT MAX(f.odometer) FROM fuels f WHERE f.vehicle_id = v.id) AS latest_odometer,
    (SELECT AVG(f.fuel_economy) FROM fuels f WHERE f.vehicle_id = v.id) AS average_fuel_economy
    FROM vehicles v
"""
    )
    fun getAllWithStats(): Flow<List<VehicleWithStats>>

    @Transaction
    @Query(
        """
    SELECT v.*,
    (SELECT MAX(f.odometer) FROM fuels f WHERE f.vehicle_id = v.id) AS latest_odometer,
    (SELECT AVG(f.fuel_economy) FROM fuels f WHERE f.vehicle_id = v.id) AS average_fuel_economy
    FROM vehicles v WHERE v.id = :id
"""
    )
    suspend fun getByIdWithStats(id: Long): VehicleWithStats?
}