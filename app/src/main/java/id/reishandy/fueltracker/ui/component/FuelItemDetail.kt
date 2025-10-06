package id.reishandy.fueltracker.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import id.reishandy.fueltracker.R
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme

@Composable
fun FuelItemDetail(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    value: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.padding_extra_small)
        )
    ) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview
@Composable
fun FuelItemDetailPreview() {
    FuelTrackerTheme(darkTheme = true) {
        FuelItemDetail(
            modifier = Modifier,
            label = R.string.odometer,
            value = "12345 km"
        )
    }
}