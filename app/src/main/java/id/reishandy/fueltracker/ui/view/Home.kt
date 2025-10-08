package id.reishandy.fueltracker.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
    onProfileClick: () -> Unit = { },
    name: String? = null,
    profilePhotoUrl: String? = null,
    shouldExit: Boolean = false
) {
    var isHeaderVisible by remember { mutableStateOf(false) }
    var areItemsVisible by remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()

    LaunchedEffect(shouldExit) {
        if (shouldExit) {
            areItemsVisible = false
            kotlinx.coroutines.delay(300)
            isHeaderVisible = false
        } else {
            isHeaderVisible = true
            kotlinx.coroutines.delay(300)
            areItemsVisible = true
        }
    }

    val fabScale by animateFloatAsState(
        targetValue = if (areItemsVisible && !shouldExit) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "fab_scale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            AnimatedVisibility(
                visible = isHeaderVisible && !shouldExit,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 600)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 400)
                )
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
                ) {
                    HomeHeader(
                        onProfileClick = onProfileClick,
                        name = name,
                        profilePhotoUrl = profilePhotoUrl
                    )

                    SectionDivider(
                        title = R.string.your_vehicles
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                state = lazyListState
            ) {
                if (vehiclesWithStats.isNotEmpty()) {
                    itemsIndexed(
                        items = vehiclesWithStats,
                        key = { _, it -> it.vehicle.id }) { index, vehicleWithStats ->
                        val delay = index * 100
                        val reverseDelay = (vehiclesWithStats.size - 1 - index) * 80

                        AnimatedVisibility(
                            visible = areItemsVisible && !shouldExit,
                            enter = slideInVertically(
                                initialOffsetY = { it / 2 },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            ) + fadeIn(
                                animationSpec = tween(durationMillis = 600, delayMillis = delay)
                            ),
                            exit = slideOutVertically(
                                targetOffsetY = { it / 2 },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            ) + fadeOut(
                                animationSpec = tween(
                                    durationMillis = 400,
                                    delayMillis = reverseDelay
                                )
                            )
                        ) {
                            VehicleItem(
                                vehicleWithStats = vehicleWithStats,
                                onClick = { onVehicleClick(vehicleWithStats) },
                                onEditClick = onEditVehicleClick,
                                onDeleteClick = onDeleteVehicleClick
                            )
                        }
                    }
                } else {
                    item {
                        AnimatedVisibility(
                            visible = areItemsVisible && !shouldExit,
                            enter = fadeIn(
                                animationSpec = tween(durationMillis = 800, delayMillis = 300)
                            ) + slideInVertically(
                                initialOffsetY = { it / 2 },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            ),
                            exit = slideOutVertically(
                                targetOffsetY = { it / 2 },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            ) + fadeOut(
                                animationSpec = tween(durationMillis = 400)
                            )
                        ) {
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
                .padding(dimensionResource(R.dimen.padding_small))
                .scale(fabScale),
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
                        latestOdometer = 150000,
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
                        latestOdometer = 25000,
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
                        latestOdometer = 12000,
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
                        latestOdometer = 220000,
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