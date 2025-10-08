package id.reishandy.fueltracker.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import id.reishandy.fueltracker.R

@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier,
    profilePhotoUrl: String?,
    contentDescription: String?,
    size: Dp = dimensionResource(R.dimen.avatar_size_small)
) {
    if (!profilePhotoUrl.isNullOrEmpty()) {
        AsyncImage(
            model = profilePhotoUrl,
            contentDescription = contentDescription,
            modifier = modifier
                .size(size)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            fallback = painterResource(R.drawable.ic_person), // Optional fallback
            error = painterResource(R.drawable.ic_person) // Optional error image
        )
    } else {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = contentDescription,
            modifier = modifier
                .size(size)
                .clip(CircleShape),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
