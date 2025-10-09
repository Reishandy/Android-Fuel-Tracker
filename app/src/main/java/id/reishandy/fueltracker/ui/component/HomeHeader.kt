package id.reishandy.fueltracker.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import id.reishandy.fueltracker.R
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = { },
    name: String? = null,
    profilePhotoUrl: String? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.shadow_elevation)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.reishandy_s),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Column(
                modifier = Modifier.clickable { onProfileClick() },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_extra_small))
            ) {
                ProfileAvatar(
                    profilePhotoUrl = profilePhotoUrl,
                    contentDescription = stringResource(R.string.default_avatar)
                )

                Text(
                    text = name?.trim()?.split(" ")?.firstOrNull()?.takeIf { it.isNotEmpty() }
                        ?: stringResource(R.string.login),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewHomeHeader() {
    FuelTrackerTheme(darkTheme = true) {
        HomeHeader()
    }
}