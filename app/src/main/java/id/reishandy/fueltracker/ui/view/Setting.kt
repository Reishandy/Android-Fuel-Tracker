package id.reishandy.fueltracker.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import id.reishandy.fueltracker.ui.component.LocaleDropdown
import id.reishandy.fueltracker.ui.component.SectionDivider
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme
import java.util.Locale

@Composable
fun Setting(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = { },
    onLoginClick: () -> Unit = { },
    onLogoutClick: () -> Unit = { },
    isLoggedIn: Boolean = true, // TODO: Change this to real authentication state
    locales: List<Locale> = listOf(Locale("en"), Locale("id")),
    selectedLocale: Locale = Locale.getDefault(),
    onLocaleSelected: (Locale) -> Unit = { }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onBackClick,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = dimensionResource(R.dimen.shadow_elevation)
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                )
            }

            Button(
                onClick = if (isLoggedIn) onLogoutClick else onLoginClick,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = if (isLoggedIn) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (isLoggedIn) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = dimensionResource(R.dimen.shadow_elevation)
                )
            ) {
                Text(text = stringResource(if (isLoggedIn) R.string.logout else R.string.login))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.padding_extra_large)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            // TODO: Replace with user avatar
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(R.string.default_avatar),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.avatar_size_large))
                    .clip(CircleShape),
            )

            // TODO: Replace with user name
            Text(
                text = "Muhammad Akbar Reishandy",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "akbar@reishandy.id",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        SectionDivider(title = R.string.settings)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(R.dimen.padding_medium),
                    horizontal = dimensionResource(R.dimen.padding_small)
                ),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(2 / 3f),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_extra_small))
            ) {
                Text(
                    text = stringResource(R.string.locale),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = stringResource(R.string.locale_description),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // TODO: Replace with real locale options
            LocaleDropdown(
                modifier = Modifier.weight(1 / 3f),
                locales = locales,
                selectedLocale = selectedLocale,
                onOptionSelected = onLocaleSelected
            )
        }
    }
}

@Preview
@Composable
fun SettingPreview() {
    FuelTrackerTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Setting()
        }
    }
}