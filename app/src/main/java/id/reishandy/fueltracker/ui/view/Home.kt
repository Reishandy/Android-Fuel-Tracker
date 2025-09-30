package id.reishandy.fueltracker.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import id.reishandy.fueltracker.R
import id.reishandy.fueltracker.ui.component.HomeHeader
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme

@Composable
fun Home(
    modifier: Modifier = Modifier,
    onAddButtonClick : () -> Unit = { }
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Column {
            HomeHeader()


        }

        FloatingActionButton(
            onClick = onAddButtonClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.fab_icon))
        }
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