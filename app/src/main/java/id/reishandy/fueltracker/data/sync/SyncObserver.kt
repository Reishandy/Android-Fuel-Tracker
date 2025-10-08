package id.reishandy.fueltracker.data.sync

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import id.reishandy.fueltracker.data.fuel.FuelDao
import id.reishandy.fueltracker.data.vehicle.VehicleDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncObserver @Inject constructor(
    private val vehicleDao: VehicleDao,
    private val fuelDao: FuelDao,
    private val dataSyncService: DataSyncService
) {
    private var syncJob: Job? = null
    private var syncScope: CoroutineScope? = null
    private val backupTrigger = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)

    @OptIn(FlowPreview::class)
    fun startSyncing() {
        stopSyncing()
        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        syncScope = scope

        // Trigger backup on any change
        vehicleDao.getAll().onEach { backupTrigger.tryEmit(Unit) }.launchIn(scope)
        fuelDao.getAll().onEach { backupTrigger.tryEmit(Unit) }.launchIn(scope)

        // Debounce and backup once
        backupTrigger
            .debounce(2000)
            .onEach {
                if (Firebase.auth.currentUser != null) {
                    dataSyncService.backupToCloud()
                }
            }
            .launchIn(scope)
    }

    fun stopSyncing() {
        syncJob?.cancel()
        syncJob = null
        syncScope?.cancel()
        syncScope = null
    }
}