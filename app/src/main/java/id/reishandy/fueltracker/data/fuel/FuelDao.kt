package id.reishandy.fueltracker.data.fuel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FuelDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(fuel: Fuel): Long

    @Delete
    suspend fun delete(fuel: Fuel)

    @Update
    suspend fun update(fuel: Fuel)

    @Query("SELECT * FROM fuels ORDER BY date DESC")
    fun getAll(): Flow<List<Fuel>>

    @Query("SELECT * FROM fuels WHERE id = :id")
    suspend fun getById(id: Long): Fuel?

    @Query("SELECT * FROM fuels WHERE vehicle_id = :vehicleId ORDER BY date DESC")
    fun getByVehicleId(vehicleId: Long): Flow<List<Fuel>>
}