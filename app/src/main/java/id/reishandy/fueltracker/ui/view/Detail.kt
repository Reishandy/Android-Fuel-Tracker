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
import androidx.compose.foundation.lazy.items
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
import id.reishandy.fueltracker.data.fuel.Fuel
import id.reishandy.fueltracker.data.vehicle.Vehicle
import id.reishandy.fueltracker.data.vehicle.VehicleWithStats
import id.reishandy.fueltracker.ui.component.DetailHeader
import id.reishandy.fueltracker.ui.component.DetailStats
import id.reishandy.fueltracker.ui.component.DetailTopButtons
import id.reishandy.fueltracker.ui.component.FuelItem
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
            id = "1",
            name = "Placeholder",
            manufacturer = "Placeholder",
            model = "Placeholder",
            year = 0,
            maxFuelCapacity = 0.0
        ),
        latestOdometer = 0,
        averageFuelEconomy = 0.0,
        totalFuelAdded = 0.0,
        totalSpent = 0.0,
        refuelCount = 0,
        refuelPerMonth = 0.0,
        avgLiterRefueled = 0.0,
        avgSpentPerRefuel = 0.0
    ),
    fuels: List<Fuel> = emptyList(),
    onAddFuelClick: () -> Unit = { },
    onFuelEditClick: (Fuel) -> Unit = { _ -> },
    onFuelDeleteClick: (Fuel) -> Unit = { _ -> },
    shouldExit: Boolean = false
) {
    var isHeaderVisible by remember { mutableStateOf(false) }
    var areItemsVisible by remember { mutableStateOf(false) }
    val expandedFuelIds = remember { mutableStateOf(setOf<String>()) }

    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        isHeaderVisible = true
        kotlinx.coroutines.delay(200)
        areItemsVisible = true
    }

    LaunchedEffect(shouldExit) {
        if (shouldExit) {
            areItemsVisible = false
            kotlinx.coroutines.delay(100)
            isHeaderVisible = false
        }
    }

    val fabScale by animateFloatAsState(
        targetValue = if (areItemsVisible && !shouldExit) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
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
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 600)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 400)
                )
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
                        vehicleWithStats = vehicleWithStats,
                        refuelRecorded = fuels.size
                    )
                }
            }

            AnimatedVisibility(
                visible = areItemsVisible && !shouldExit,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 600)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 400)
                )
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                    state = lazyListState
                ) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
                        ) {
                            SectionDivider(title = R.string.vehicle_stats)

                            DetailStats(
                                vehicleWithStats = vehicleWithStats
                            )

                            SectionDivider(title = R.string.refuel_history)
                        }
                    }

                    items(
                        items = fuels,
                        key = { it.id }
                    ) { fuel ->
                        FuelItem(
                            fuel = fuel,
                            onEditClick = { onFuelEditClick(fuel) },
                            onDeleteClick = { onFuelDeleteClick(fuel) },
                            expanded = expandedFuelIds.value.contains(fuel.id),
                            onExpandedChange = { isExpanded ->
                                expandedFuelIds.value = if (isExpanded) {
                                    expandedFuelIds.value + fuel.id
                                } else {
                                    expandedFuelIds.value - fuel.id
                                }
                            },
                            modifier = Modifier.animateItem()
                        )
                    }

                    if (fuels.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.nothing_to_see_here),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem(),
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
        }

        ExtendedFloatingActionButton(
            onClick = onAddFuelClick,
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
                        id = "1",
                        name = "My Motorcycle",
                        manufacturer = "Honda",
                        model = "PCX 160",
                        year = 2024,
                        maxFuelCapacity = 8.0
                    ),
                    latestOdometer = 15000,
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