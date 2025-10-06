package id.reishandy.fueltracker.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import id.reishandy.fueltracker.R
import id.reishandy.fueltracker.data.vehicle.Vehicle
import id.reishandy.fueltracker.data.vehicle.VehicleWithStats
import id.reishandy.fueltracker.ui.component.DetailCard
import id.reishandy.fueltracker.ui.component.DetailHeader
import id.reishandy.fueltracker.ui.component.DetailStats
import id.reishandy.fueltracker.ui.component.DetailTopButtons
import id.reishandy.fueltracker.ui.component.SectionDivider
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun Detail(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = { },
    onEditClick: () -> Unit = { },
    onDeleteClick: () -> Unit = { },
    vehicleWithStats: VehicleWithStats = VehicleWithStats(
        vehicle = Vehicle(
            id = 1,
            name = "Placeholder",
            manufacturer = "Placeholder",
            model = "Placeholder",
            year = 0,
            maxFuelCapacity = 0.0
        ),
        latestOdometer = 0.0,
        averageFuelEconomy = 0.0,
        totalFuelAdded = 0.0,
        totalSpent = 0.0,
        refuelCount = 0,
        refuelPerMonth = 0.0,
        avgLiterRefueled = 0.0,
        avgSpentPerRefuel = 0.0
    ),
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            DetailTopButtons(
                onBackClick = onBackClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )

            DetailHeader(
                vehicleWithStats = vehicleWithStats
            )

            SectionDivider(title = R.string.vehicle_stats)

            DetailStats(
                vehicleWithStats = vehicleWithStats
            )

            SectionDivider(title = R.string.refuel_history)
        }
    }
}

@Preview
@Composable
fun DetailPreview() {
    FuelTrackerTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Detail(
                vehicleWithStats = VehicleWithStats(
                    vehicle = Vehicle(
                        id = 1,
                        name = "My Motorcycle",
                        manufacturer = "Honda",
                        model = "PCX 160",
                        year = 2024,
                        maxFuelCapacity = 8.0
                    ),
                    latestOdometer = 15000.0,
                    averageFuelEconomy = 39.5,
                    totalFuelAdded = 120.0,
                    totalSpent = 1500000.0,
                    refuelCount = 12,
                    refuelPerMonth = 2.5,
                    avgLiterRefueled = 7.0,
                    avgSpentPerRefuel = 85000.0
                )
            )
        }
    }
}