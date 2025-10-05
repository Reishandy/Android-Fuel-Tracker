package id.reishandy.fueltracker.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import id.reishandy.fueltracker.R
import id.reishandy.fueltracker.data.vehicle.Vehicle
import id.reishandy.fueltracker.data.vehicle.VehicleWithStats
import id.reishandy.fueltracker.ui.component.DetailTopButtons
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
                averageFuelEconomy = 0.0
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

            Text(text = vehicleWithStats.vehicle.name)
        }
    }
}

@Preview
@Composable
fun DetailPreview() {
    FuelTrackerTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Detail()
        }
    }
}