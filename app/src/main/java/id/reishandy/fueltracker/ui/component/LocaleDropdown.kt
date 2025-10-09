package id.reishandy.fueltracker.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import id.reishandy.fueltracker.R
import id.reishandy.fueltracker.ui.theme.FuelTrackerTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocaleDropdown(
    modifier: Modifier = Modifier,
    locales: List<Locale> = listOf(Locale("en"), Locale("id")),
    selectedLocale: Locale = Locale.getDefault(),
    onOptionSelected: (Locale) -> Unit = { }
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(selectedLocale.getDisplayName(selectedLocale)) }

    val filteredLocales = remember(searchText, locales) {
        if (searchText.isBlank()) {
            locales
        } else {
            locales.filter {
                it.getDisplayName(it).contains(searchText, ignoreCase = true) ||
                        it.toLanguageTag().contains(searchText, ignoreCase = true)
            }
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        TextField(
            value = searchText,
            onValueChange = { newText ->
                searchText = newText
                if (!expanded) expanded = true
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
                .heightIn(max = dimensionResource(R.dimen.locale_dropdown_height))
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (filteredLocales.isEmpty()) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.no_results)) },
                    onClick = { },
                    enabled = false
                )
            } else {
                filteredLocales.forEach { locale ->
                    DropdownMenuItem(
                        text = { Text(locale.getDisplayName(locale)) },
                        onClick = {
                            onOptionSelected(locale)
                            searchText = locale.getDisplayName(locale)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LocaleDropdownPreview() {
    FuelTrackerTheme(darkTheme = true) {
        LocaleDropdown(
            modifier = Modifier.fillMaxWidth(),
            onOptionSelected = {}
        )
    }
}
