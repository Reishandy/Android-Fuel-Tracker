package id.reishandy.fueltracker.data.sync

import android.util.Log
import androidx.room.withTransaction
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import id.reishandy.fueltracker.data.FuelTrackerAppDatabase
import id.reishandy.fueltracker.data.fuel.Fuel
import id.reishandy.fueltracker.data.vehicle.Vehicle
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSyncService @Inject constructor(
    private val db: FuelTrackerAppDatabase,
    private val firestore: FirebaseFirestore
) {

    private val userId: String
        get() = Firebase.auth.currentUser?.uid
            ?: throw IllegalStateException("User not signed in to Firebase")

    private companion object {
        const val TAG = "DataSyncService"
    }

    /**
     * Restore from cloud ONLY if cloud has data.
     * If cloud is empty, keep local data.
     */
    suspend fun syncFromCloud() {
        try {
            val vehicleRef = firestore.collection("users/$userId/vehicles")
            val fuelRef = firestore.collection("users/$userId/fuel")

            val vehicleSnapshot = vehicleRef.get().await()
            val fuelSnapshot = fuelRef.get().await()

            val hasCloudData = vehicleSnapshot.size() > 0 || fuelSnapshot.size() > 0

            if (hasCloudData) {
                val vehicles = vehicleSnapshot.toObjects(Vehicle::class.java)
                val fuels = fuelSnapshot.toObjects(Fuel::class.java)

                db.withTransaction {
                    db.vehicleDao().deleteAll()
                    db.fuelDao().deleteAll()
                    if (vehicles.isNotEmpty()) db.vehicleDao().insertAll(vehicles)
                    if (fuels.isNotEmpty()) db.fuelDao().insertAll(fuels)
                }
                Log.d(TAG, "Restored ${vehicles.size} vehicles and ${fuels.size} fuels from cloud")
            } else {
                Log.d(TAG, "Cloud is empty — keeping local data")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to sync from cloud", e)
            throw e
        }
    }

    /**
     * Backup local DB to Firestore (replace cloud data)
     */
    suspend fun backupToCloud() {
        if (Firebase.auth.currentUser == null) {
            Log.w(TAG, "Skipping backup: user not signed in")
            return
        }

        try {
            // Get current local data (assuming DAO returns List)
            val vehicles = db.vehicleDao().getAllSnapshot()
            val fuels = db.fuelDao().getAllSnapshot()

            val vehicleRef = firestore.collection("users/$userId/vehicles")
            val fuelRef = firestore.collection("users/$userId/fuel")

            // TODO: Optimize by batching writes or using Firestore bulk operations
            // Clear existing cloud data
            val existingVehicles = vehicleRef.get().await()
            existingVehicles.documents.forEach { it.reference.delete().await() }

            val existingFuels = fuelRef.get().await()
            existingFuels.documents.forEach { it.reference.delete().await() }

            // Upload new data
            vehicles.forEach { vehicle ->
                vehicleRef.add(vehicle).await()
            }
            fuels.forEach { fuel ->
                fuelRef.add(fuel).await()
            }

            Log.d(TAG, "Backed up ${vehicles.size} vehicles and ${fuels.size} fuels to cloud")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to backup to cloud", e)
            throw e
        }
    }
}