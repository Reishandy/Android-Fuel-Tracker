package id.reishandy.fueltracker.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import id.reishandy.fueltracker.R
import id.reishandy.fueltracker.data.vehicle.Vehicle
import id.reishandy.fueltracker.data.vehicle.VehicleWithStats
import id.reishandy.fueltracker.util.formatCurrency
import id.reishandy.fueltracker.util.formatNumber
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme

@Composable
fun DetailStats(
    modifier: Modifier = Modifier,
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
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            DetailCard(
                modifier = Modifier.weight(2 / 3f),
                title = R.string.odometer,
                value = stringResource(
                    R.string.km_abbr_value,
                    formatNumber(vehicleWithStats.latestOdometer)
                )
            )

            DetailCard(
                modifier = Modifier.weight(1 / 3f),
                title = R.string.max_fuel,
                value = stringResource(
                    R.string.liter_abbr_value,
                    formatNumber(vehicleWithStats.vehicle.maxFuelCapacity, 1)
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            DetailCard(
                modifier = Modifier.weight(1 / 3f),
                title = R.string.fuel_economy,
                value = stringResource(
                    R.string.kml_abbr_value,
                    formatNumber(vehicleWithStats.averageFuelEconomy, 2)
                )
            )

            DetailCard(
                modifier = Modifier.weight(1 / 3f),
                title = R.string.est_range,
                value = stringResource(
                    R.string.km_abbr_value,
                        formatNumber(vehicleWithStats.averageFuelEconomy * vehicleWithStats.vehicle.maxFuelCapacity)
                )
            )

            DetailCard(
                modifier = Modifier.weight(1 / 3f),
                title = R.string.refuel_per_month,
                value = stringResource(
                    R.string.refuel_per_month_value,
                        formatNumber(vehicleWithStats.refuelPerMonth, 1)
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            DetailCard(
                modifier = Modifier.weight(1 / 3f),
                title = R.string.avg_refueled,
                value = stringResource(
                    R.string.liter_abbr_value,
                    formatNumber(vehicleWithStats.avgLiterRefueled, 1)
                )
            )

            DetailCard(
                modifier = Modifier.weight(2 / 3f),
                title = R.string.avg_spent_per_refuel,
                value = formatCurrency(vehicleWithStats.avgSpentPerRefuel)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            DetailCard(
                modifier = Modifier.weight(1 / 3f),
                title = R.string.total_refuel,
                value = stringResource(
                    R.string.liter_abbr_value,
                    formatNumber(vehicleWithStats.totalFuelAdded, 1)
                )
            )

            DetailCard(
                modifier = Modifier.weight(2 / 3f),
                title = R.string.total_spent,
                value = formatCurrency(vehicleWithStats.totalSpent)
            )
        }
    }
}

@Preview
@Composable
fun DetailStatsPreview() {
    FuelTrackerTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            DetailStats(
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