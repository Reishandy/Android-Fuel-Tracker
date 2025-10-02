package id.reishandy.fueltracker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.reishandy.fueltracker.data.FuelTrackerAppDatabase
import id.reishandy.fueltracker.data.vehicle.VehicleDao
import id.reishandy.fueltracker.data.vehicle.VehicleRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FuelTrackerAppDatabase =
        Room.databaseBuilder(context, FuelTrackerAppDatabase::class.java, "fueltracker_db")
            .fallbackToDestructiveMigration(dropAllTables = false)
            .build()

    @Provides
    fun provideVehicleDao(db: FuelTrackerAppDatabase): VehicleDao = db.vehicleDao()

    @Provides
    @Singleton
    fun provideVehicleRepository(dao: VehicleDao): VehicleRepository =
        VehicleRepository(dao)
}