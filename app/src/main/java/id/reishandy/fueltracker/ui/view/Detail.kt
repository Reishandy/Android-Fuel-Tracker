package id.reishandy.fueltracker.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import id.reishandy.fueltracker.ui.component.DetailHeader
import id.reishandy.fueltracker.ui.component.DetailStats
import id.reishandy.fueltracker.ui.component.DetailTopButtons
import id.reishandy.fueltracker.ui.component.SectionDivider
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme

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
    onAddFuelClick: () -> Unit = { }
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

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
            ) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
                    ) {
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

                if (false) {
                    // TODO: Implement recent refuel list
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
            onClick = onAddFuelClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(dimensionResource(R.dimen.padding_small)),
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.fab_icon)
                )
            },
            text = { Text(text = stringResource(R.string.record_refuel)) },
            shape = MaterialTheme.shapes.medium
        )
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