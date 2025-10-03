package id.reishandy.fueltracker.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import id.reishandy.fueltracker.ui.component.HomeHeader
import id.reishandy.fueltracker.ui.component.VehicleItem
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme

@Composable
fun Home(
    modifier: Modifier = Modifier,
    onAddVehicleClick: () -> Unit = { },
    onEditVehicleClick: (Vehicle) -> Unit = { _ -> },
    onDeleteVehicleClick: (Vehicle) -> Unit = { _ -> }
) {
    // TODO: Receive list of vehicles from ViewModel
    // TODO: Handle Login
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            HomeHeader()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_small)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                Text(
                    text = "Your Vehicles",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                HorizontalDivider()
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
            ) {
                item {
                    VehicleItem(
                        vehicle = Vehicle(
                            id = 1,
                            name = "My Car",
                            manufacturer = "Toyota",
                            model = "Avanza",
                            year = 2020,
                            maxFuelCapacity = 45.0
                        ),
                        onEditClick = onEditVehicleClick,
                        onDeleteClick = onDeleteVehicleClick
                    )
                }

                item {
                    VehicleItem(
                        vehicle = Vehicle(
                            id = 2,
                            name = "My Motor",
                            manufacturer = "Yamaha",
                            model = "Nmax",
                            year = 2021,
                            maxFuelCapacity = 7.1
                        ),
                        onEditClick = onEditVehicleClick,
                        onDeleteClick = onDeleteVehicleClick
                    )
                }

                items(10) { index ->
                    VehicleItem(
                        onEditClick = onEditVehicleClick,
                        onDeleteClick = onDeleteVehicleClick
                    )
                }

                // TODO: Show this when list is empty
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
        )
    }
}

@Preview
@Composable
internal fun PreviewHome() {
    FuelTrackerTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Home()
        }
    }
}