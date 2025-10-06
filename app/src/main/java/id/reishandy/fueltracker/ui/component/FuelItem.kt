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
import androidx.compose.ui.tooling.preview.Preview
import id.reishandy.fueltracker.R
import id.reishandy.fueltracker.data.fuel.Fuel
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FuelItem(
    modifier: Modifier = Modifier,
    fuel: Fuel = Fuel(
        id = 0,
        vehicleId = 0,
        date = 0,
        odometer = 0.0,
        trip = 0.0,
        fuelAdded = 0.0,
        fuelType = "",
        pricePerLiter = 0.0,
        totalCost = 0.0,
        fuelEconomy = 0.0,
        costPerKm = 0.0,
        fuelRemaining = 0.0
    ),
    onEditClick: (Fuel) -> Unit = { _ -> },
    onDeleteClick: (Fuel) -> Unit = { _ -> }
) {
    var expanded by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { expanded = !expanded },
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Card(
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.padding_extra_small)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "Jan 15, 2024",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(R.dimen.padding_small),
                        vertical = dimensionResource(R.dimen.padding_extra_small)
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(0.6f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = NumberFormat.getCurrencyInstance(Locale.getDefault())
                            .format(fuel.totalCost),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = stringResource(
                            R.string.per_liter_abbr_value,
                            NumberFormat.getCurrencyInstance(Locale.getDefault())
                                .format(fuel.pricePerLiter)
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Column(
                    modifier = Modifier.weight(0.5f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {


                    Text(
                        text = stringResource(
                            R.string.liter_abbr_value,
                            fuel.fuelAdded
                        ),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = fuel.fuelType,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
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
                        onEditClick(fuel)
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
                        onDeleteClick(fuel)
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
internal fun PreviewFuelItem() {
    FuelTrackerTheme(darkTheme = true) {
        FuelItem(
            fuel = Fuel(
                id = 1,
                vehicleId = 1,
                date = 0,
                odometer = 15000.0,
                trip = 250.0,
                fuelAdded = 5.0,
                fuelType = "Pertalite",
                pricePerLiter = 10_000.0,
                totalCost = 50_000.0,
                fuelEconomy = 50.0,
                costPerKm = 200.0,
                fuelRemaining = 3.0
            )
        )
    }
}