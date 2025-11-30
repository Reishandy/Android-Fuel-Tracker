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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fuel: Fuel): Long

    @Delete
    suspend fun delete(fuel: Fuel)

    @Update
    suspend fun update(fuel: Fuel)

    @Query("SELECT * FROM fuels ORDER BY date DESC, created_at DESC")
    fun getAll(): Flow<List<Fuel>>

    @Query("SELECT * FROM fuels WHERE id = :id")
    suspend fun getById(id: String): Fuel?

    @Query("SELECT * FROM fuels WHERE vehicle_id = :vehicleId ORDER BY date DESC, created_at DESC")
    fun getByVehicleId(vehicleId: String): Flow<List<Fuel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(fuels: List<Fuel>)

    @Query("DELETE FROM fuels")
    suspend fun deleteAll()

    @Query("SELECT * FROM fuels ORDER BY date DESC, created_at DESC")
    suspend fun getAllSnapshot(): List<Fuel>

    // New helper to fetch the immediate previous entry for a vehicle relative to a given (date, created_at)
    @Query("SELECT * FROM fuels WHERE vehicle_id = :vehicleId AND (date < :date OR (date = :date AND created_at < :createdAt)) ORDER BY date DESC, created_at DESC LIMIT 1")
    suspend fun getPreviousEntry(vehicleId: String, date: Long, createdAt: Long): Fuel?
}