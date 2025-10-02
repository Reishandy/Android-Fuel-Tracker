package id.reishandy.fueltracker.data.vehicle

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("SELECT * FROM vehicle ORDER BY created_at DESC")
        fun getAllVehicles(): Flow<List<Vehicle>>

    @Query("SELECT * FROM vehicle WHERE id = :id")
    suspend fun getVehicleById(id: Long): Vehicle?
}