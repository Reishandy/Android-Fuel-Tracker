package id.reishandy.fueltracker.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import id.reishandy.fueltracker.R
import id.reishandy.fueltracker.ui.component.LocaleDropdown
import id.reishandy.fueltracker.ui.component.ProfileAvatar
import id.reishandy.fueltracker.ui.component.SectionDivider
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme
import java.util.Locale

@Composable
fun Setting(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = { },
    onLoginClick: () -> Unit = { },
    onLogoutClick: () -> Unit = { },
    name: String? = null,
    email: String? = null,
    profilePhotoUrl: String? = null,
    locales: List<Locale> = listOf(Locale("en"), Locale("id")),
    selectedLocale: Locale = Locale.getDefault(),
    onLocaleSelected: (Locale) -> Unit = { },
    isProcessing: Boolean = false
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
                onClick = if (email != null) onLogoutClick else onLoginClick,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = if (email != null) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (email != null) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = dimensionResource(R.dimen.shadow_elevation)
                ),
                enabled = !isProcessing
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.progress_indicator_size)),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    Text(text = stringResource(if (email != null) R.string.logout else R.string.login))
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.padding_large)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            ProfileAvatar(
                profilePhotoUrl = profilePhotoUrl,
                contentDescription = stringResource(R.string.default_avatar),
                size = dimensionResource(R.dimen.avatar_size_large)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                Text(
                    text = name ?: stringResource(R.string.login),
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = email ?: "",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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