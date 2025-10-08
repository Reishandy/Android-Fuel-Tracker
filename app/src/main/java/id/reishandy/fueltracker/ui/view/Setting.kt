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
    isProcessing: Boolean = false,
    shouldExit: Boolean = false,
) {
    var isHeaderVisible by remember { mutableStateOf(false) }
    var isProfileVisible by remember { mutableStateOf(false) }
    var isSettingsVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isHeaderVisible = true
        kotlinx.coroutines.delay(200)
        isProfileVisible = true
        kotlinx.coroutines.delay(200)
        isSettingsVisible = true
    }

    LaunchedEffect(shouldExit) {
        if (shouldExit) {
            isSettingsVisible = false
            kotlinx.coroutines.delay(100)
            isProfileVisible = false
            kotlinx.coroutines.delay(200)
            isHeaderVisible = false
        }
    }

    val buttonScale by animateFloatAsState(
        targetValue = if (isHeaderVisible && !shouldExit) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "button_scale"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
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
                    ),
                    modifier = Modifier.scale(buttonScale)
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
                    enabled = !isProcessing,
                    modifier = Modifier.scale(buttonScale)
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
        }

        AnimatedVisibility(
            visible = isProfileVisible && !shouldExit,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn(
                animationSpec = tween(durationMillis = 600, delayMillis = 100)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it / 2 },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeOut(
                animationSpec = tween(durationMillis = 400)
            )
        ) {
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
        }

        AnimatedVisibility(
            visible = isSettingsVisible && !shouldExit,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn(
                animationSpec = tween(durationMillis = 600, delayMillis = 200)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it / 2 },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeOut(
                animationSpec = tween(durationMillis = 400)
            )
        ) {
            Column {
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

                    LocaleDropdown(
                        modifier = Modifier.weight(1 / 3f),
                        locales = locales,
                        selectedLocale = selectedLocale,
                        onOptionSelected = onLocaleSelected
                    )
                }
            }
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