package id.reishandy.fueltracker.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import id.reishandy.fueltracker.R
import id.reishandy.fueltracker.util.PastOrPresentSelectableDates
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun DatePickerModal(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    // IDK what is happening here, but without this offset adjustment,
    // the selected date will be one day earlier than it should be
    // So I'm keeping this here
    val currentZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault())
    val zoneOffset = currentZonedDateTime.offset
    val initialDateMillis = currentZonedDateTime.toInstant().toEpochMilli() + zoneOffset.totalSeconds * 1000

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis,
        selectableDates = PastOrPresentSelectableDates
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis ?: initialDateMillis)
                onDismiss()
            }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Preview
@Composable
fun DatePickerModalPreview() {
    FuelTrackerTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            DatePickerModal(onDateSelected = {}, onDismiss = {})
        }
    }
}