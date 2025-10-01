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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import id.reishandy.fueltracker.R
import id.reishandy.fueltracker.ui.component.HomeHeader
import id.reishandy.fueltracker.ui.component.VehicleFormBottomSheet
import id.reishandy.fueltracker.ui.component.VehicleItem
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    modifier: Modifier = Modifier,
    onFloatingButtonClick: () -> Unit = { },
) {
    // TODO: Move to ui state?
    var showSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

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
                items(10) { index ->
                    VehicleItem()
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
            onClick = {
                scope.launch {
                    showSheet = true
                    sheetState.show()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(dimensionResource(R.dimen.padding_small)),
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.fab_icon)
                )
            },
            text = { Text(text = "Add Vehicle") },
        )

        VehicleFormBottomSheet(
            onDismissRequest = {
                showSheet = false
            },
            onCloseButtonClick = {
                scope.launch {
                    sheetState.hide()
                    showSheet = false
                }
            },
            sheetState = sheetState,
            showSheet = showSheet
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