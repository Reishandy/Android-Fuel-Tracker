package id.reishandy.fueltracker.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.reishandy.fueltracker.R
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleFormBottomSheet(
    modifier: Modifier = Modifier,
    onSaveButtonClick: () -> Unit = { },
    onDismissRequest: () -> Unit = { },
    onCloseButtonClick: () -> Unit = { },
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    ),
    showSheet: Boolean = true,
) {
    // TODO: Close button
    // TODO: Handle keyboard overlap

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            ) {
                Text(
                    text = stringResource(R.string.add_new_vehicle),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Column(
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_large)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
                ) {
                    // TODO: Handle all state by ui state
                    TextField(
                        value = "",
                        onValueChange = { },
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.vehicle_name)) },
                        placeholder = { Text(text = stringResource(R.string.vehicle_name_placeholder)) },
                        leadingIcon = {
                             Icon(imageVector = Icons.Default.DirectionsCar, contentDescription = null)
                        },
                        supportingText = {
                            Text(text = "Error message")
                        },
                        isError = false,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                    )

                    TextField(
                        value = "",
                        onValueChange = { },
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.manufacturer)) },
                        placeholder = { Text(text = stringResource(R.string.manufacturer_placeholder)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Construction, contentDescription = null)
                        },
                        supportingText = {
                            Text(text = "Error message")
                        },
                        isError = false,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                    )

                    TextField(
                        value = "",
                        onValueChange = { },
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.model)) },
                        placeholder = { Text(text = stringResource(R.string.model_placeholder)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Build, contentDescription = null)
                        },
                        supportingText = {
                            Text(text = "Error message")
                        },
                        isError = false,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                    )

                    TextField(
                        value = "",
                        onValueChange = { },
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.year)) },
                        placeholder = { Text(text = stringResource(R.string.year_placeholder)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null)
                        },
                        supportingText = {
                            Text(text = "Error message")
                        },
                        isError = false,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next,
                        ),
                        singleLine = true,
                    )

                    TextField(
                        value = "",
                        onValueChange = { },
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.max_fuel)) },
                        placeholder = { Text(text = stringResource(R.string.max_fuel_placeholder)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.LocalGasStation, contentDescription = null)
                        },
                        suffix = {
                            Text(text = stringResource(R.string.liter_abbr))
                        },
                        supportingText = {
                            Text(text = "Error message")
                        },
                        isError = false,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done,
                        ),
                        singleLine = true,
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                ) {
                    Button(
                        onClick = onCloseButtonClick,
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }

                    Button(
                        onClick = {
                            onCloseButtonClick()
                            onSaveButtonClick()
                        },
                    ) {
                        Text(text = stringResource(R.string.save))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun VehicleFormBottomSheetPreview() {
    FuelTrackerTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            VehicleFormBottomSheet()
        }
    }
}