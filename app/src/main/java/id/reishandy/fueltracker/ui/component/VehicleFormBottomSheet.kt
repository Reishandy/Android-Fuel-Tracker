package id.reishandy.fueltracker.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.reishandy.fueltracker.R
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleFormBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = { },
    onCloseButtonClick: () -> Unit = { },
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    ),
    showSheet: Boolean = true,
) {
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_medium))
            ) {
                Text("TODO: Vehicle form")
                Spacer(Modifier.height(500.dp))
                Button(onClick = onCloseButtonClick) {
                    Text("Close Sheet")
                }
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