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
    private val fuelTrackerAppDatabase: FuelTrackerAppDatabase,
    private val firestore: FirebaseFirestore
) {
    suspend fun syncFromCloud() {
        val userId = Firebase.auth.currentUser?.uid
            ?: throw IllegalStateException("User not signed in")

        val cloudVehicles = firestore.collection("users/$userId/vehicles").get().await()
            .toObjects(Vehicle::class.java)
        val cloudFuels = firestore.collection("users/$userId/fuel").get().await()
            .toObjects(Fuel::class.java)

        fuelTrackerAppDatabase.withTransaction {
            // Merge vehicles
            val localVehicles =
                fuelTrackerAppDatabase.vehicleDao().getAllSnapshot().associateBy { it.id }
            cloudVehicles.forEach { cloudVehicle ->
                localVehicles[cloudVehicle.id]?.let { local ->
                    if (cloudVehicle.updatedAt > local.updatedAt) {
                        fuelTrackerAppDatabase.vehicleDao().update(cloudVehicle)
                    }
                    // else: keep local (newer or same)
                } ?: run {
                    fuelTrackerAppDatabase.vehicleDao().insert(cloudVehicle) // new
                }
            }

            val localFuels = fuelTrackerAppDatabase.fuelDao().getAllSnapshot().associateBy { it.id }
            cloudFuels.forEach { cloudFuel ->
                localFuels[cloudFuel.id]?.let { local ->
                    if (cloudFuel.updatedAt > local.updatedAt) {
                        fuelTrackerAppDatabase.fuelDao().update(cloudFuel)
                    }
                } ?: run {
                    fuelTrackerAppDatabase.fuelDao().insert(cloudFuel)
                }
            }

            Log.d(
                "DataSyncService",
                "Restored ${cloudVehicles.size} vehicles and ${cloudFuels.size} fuels from cloud"
            )
        }
    }

    // INFO: This is not an optimal sync strategy for large datasets. It works well for small datasets typical of personal apps.
    // For larger datasets, consider more sophisticated strategies like change tracking, timestamps, or versioning.
    suspend fun backupToCloud() {
        val userId = Firebase.auth.currentUser?.uid
            ?: throw IllegalStateException("User not signed in")

        try {
            val vehicles = fuelTrackerAppDatabase.vehicleDao().getAllSnapshot()
            val fuels = fuelTrackerAppDatabase.fuelDao().getAllSnapshot()

            val vehicleRef = firestore.collection("users/$userId/vehicles")
            val fuelRef = firestore.collection("users/$userId/fuel")

            // Step 1: Get current cloud docs to delete outdated ones
            val existingVehicleDocs = vehicleRef.get().await().documents
            val existingFuelDocs = fuelRef.get().await().documents

            // Step 2: Create a batch
            val batch = firestore.batch()

            // Delete documents not in local data
            val localVehicleIds = vehicles.map { it.id }.toSet()
            existingVehicleDocs.forEach { doc ->
                if (!localVehicleIds.contains(doc.id)) {
                    batch.delete(doc.reference)
                }
            }

            val localFuelIds = fuels.map { it.id }.toSet()
            existingFuelDocs.forEach { doc ->
                if (!localFuelIds.contains(doc.id)) {
                    batch.delete(doc.reference)
                }
            }

            // Upsert local data
            vehicles.forEach { vehicle ->
                batch.set(vehicleRef.document(vehicle.id), vehicle)
            }
            fuels.forEach { fuel ->
                batch.set(fuelRef.document(fuel.id), fuel)
            }

            // Commit all in one atomic operation
            batch.commit().await()

            Log.d("DataSyncService", "Backed up ${vehicles.size} vehicles and ${fuels.size} fuels to cloud")
        } catch (e: Exception) {
            Log.e("DataSyncService", "Backup failed", e)
            throw e
        }
    }
}