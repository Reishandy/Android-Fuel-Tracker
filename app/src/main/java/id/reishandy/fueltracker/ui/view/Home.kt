package id.reishandy.fueltracker.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import id.reishandy.fueltracker.R
import id.reishandy.fueltracker.data.vehicle.Vehicle
import id.reishandy.fueltracker.data.vehicle.VehicleWithStats
import id.reishandy.fueltracker.ui.component.HomeHeader
import id.reishandy.fueltracker.ui.component.SectionDivider
import id.reishandy.fueltracker.ui.component.VehicleItem
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme

@Composable
fun Home(
    modifier: Modifier = Modifier,
    vehiclesWithStats: List<VehicleWithStats> = emptyList(),
    onAddVehicleClick: () -> Unit = { },
    onEditVehicleClick: (Vehicle) -> Unit = { _ -> },
    onDeleteVehicleClick: (Vehicle) -> Unit = { _ -> },
    onVehicleClick: (VehicleWithStats) -> Unit = { _ -> },
    onProfileClick: () -> Unit = { }
) {
    // TODO: Handle Login
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            HomeHeader(
                onProfileClick = onProfileClick
            )

            SectionDivider(
                title = R.string.your_vehicles
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
            ) {
                if (vehiclesWithStats.isNotEmpty()) {
                    items(vehiclesWithStats) { vehicleWithStats ->
                        VehicleItem(
                            vehicleWithStats = vehicleWithStats,
                            onClick = { onVehicleClick(vehicleWithStats) },
                            onEditClick = onEditVehicleClick,
                            onDeleteClick = onDeleteVehicleClick
                        )
                    }
                } else {
                    item {
                        Text(
                            text = stringResource(R.string.nothing_to_see_here),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                }

                item {
                    Spacer(
                        modifier = Modifier
                            .padding(bottom = dimensionResource(R.dimen.bottom_spacer))
                            .fillMaxWidth()
                    )
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = onAddVehicleClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(dimensionResource(R.dimen.padding_small)),
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.fab_icon)
                )
            },
            text = { Text(text = stringResource(R.string.add_vehicle)) },
            shape = MaterialTheme.shapes.medium
        )
    }
}

@Preview
@Composable
internal fun PreviewHome() {
    FuelTrackerTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Home(
                vehiclesWithStats = listOf(
                    VehicleWithStats(
                        vehicle = Vehicle(
                            id = 1,
                            name = "My Car",
                            manufacturer = "Toyota",
                            model = "Avanza",
                            year = 2010,
                            maxFuelCapacity = 45.0
                        ),
                        latestOdometer = 150000.0,
                        averageFuelEconomy = 14.5,
                        totalFuelAdded = 400.0,
                        totalSpent = 12000000.0,
                        refuelCount = 40,
                        refuelPerMonth = 3.0,
                        avgLiterRefueled = 7.0,
                        avgSpentPerRefuel = 85000.0
                    ),
                    VehicleWithStats(
                        vehicle = Vehicle(
                            id = 2,
                            name = "My Motorcycle",
                            manufacturer = "Honda",
                            model = "Vario 150",
                            year = 2019,
                            maxFuelCapacity = 5.5
                        ),
                        latestOdometer = 25000.0,
                        averageFuelEconomy = 45.0,
                        totalFuelAdded = 150.0,
                        totalSpent = 3000000.0,
                        refuelCount = 25,
                        refuelPerMonth = 2.5,
                        avgLiterRefueled = 6.0,
                        avgSpentPerRefuel = 120000.0
                    ),
                    VehicleWithStats(
                        vehicle = Vehicle(
                            id = 3,
                            name = "My Second Motorcycle",
                            manufacturer = "Yamaha",
                            model = "NMAX 155",
                            year = 2021,
                            maxFuelCapacity = 7.1
                        ),
                        latestOdometer = 12000.0,
                        averageFuelEconomy = 42.0,
                        totalFuelAdded = 60.0,
                        totalSpent = 1800000.0,
                        refuelCount = 10,
                        refuelPerMonth = 1.5,
                        avgLiterRefueled = 6.0,
                        avgSpentPerRefuel = 180000.0
                    ),
                    VehicleWithStats(
                        vehicle = Vehicle(
                            id = 4,
                            name = "My Old Car",
                            manufacturer = "Daihatsu",
                            model = "Xenia",
                            year = 2005,
                            maxFuelCapacity = 42.0
                        ),
                        latestOdometer = 220000.0,
                        averageFuelEconomy = 10.5,
                        totalFuelAdded = 600.0,
                        totalSpent = 9000000.0,
                        refuelCount = 60,
                        refuelPerMonth = 5.0,
                        avgLiterRefueled = 10.0,
                        avgSpentPerRefuel = 150000.0
                    )
                )
            )
        }
    }
}