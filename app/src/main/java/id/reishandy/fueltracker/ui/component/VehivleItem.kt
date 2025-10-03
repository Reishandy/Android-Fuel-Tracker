package id.reishandy.fueltracker.ui.component

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import id.reishandy.fueltracker.R
import id.reishandy.fueltracker.data.vehicle.Vehicle
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme

@Composable
fun VehicleItem(
    modifier: Modifier = Modifier,
    vehicle: Vehicle = Vehicle(
        id = 1,
        name = "Placeholder",
        manufacturer = "Placeholder",
        model = "Placeholder",
        year = 0,
        maxFuelCapacity = 0.0
    ),
    odometer: String = "0",
    averageConsumption: String = "0.0",
    onClick: () -> Unit = { },
    onEditClick: (Vehicle) -> Unit = { _ -> },
    onDeleteClick: (Vehicle) -> Unit = { _ -> }
) {
    var showMenu by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { showMenu = true }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.shadow_elevation)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.6f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(
                        R.string.card_vehicle_manufacturer_model,
                        vehicle.manufacturer,
                        vehicle.model,
                        vehicle.year.toString()
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = vehicle.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(
                modifier = Modifier.weight(0.5f),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.card_vehicle_avg, averageConsumption),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Text(
                    text = stringResource(R.string.card_vehicle_odometer, odometer),
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = stringResource(
                        R.string.card_vehicle_tank_capacity,
                        vehicle.maxFuelCapacity.toString()
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Box(
            modifier = Modifier.align(Alignment.End),
        ) {
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                shape = MaterialTheme.shapes.medium,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                shadowElevation = dimensionResource(R.dimen.shadow_elevation)
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.edit)) },
                    onClick = {
                        showMenu = false
                        onEditClick(vehicle)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit)
                        )
                    },
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.delete)) },
                    onClick = {
                        showMenu = false
                        onDeleteClick(vehicle)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    },
                    colors = MenuDefaults.itemColors().copy(
                        textColor = MaterialTheme.colorScheme.error,
                        leadingIconColor = MaterialTheme.colorScheme.error
                    )
                )
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewVehicleItem() {
    FuelTrackerTheme(darkTheme = true) {
        VehicleItem(
            vehicle = Vehicle(
                id = 1,
                name = "Main Motorcycle",
                manufacturer = "Honda",
                model = "PCX 160",
                year = 2024,
                maxFuelCapacity = 8.1
            ),
            odometer = "980,000",
            averageConsumption = "39.99",
        )
    }
}