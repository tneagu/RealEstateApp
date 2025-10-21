package com.tneagu.realestateapp.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.tneagu.realestateapp.core.ui.theme.Dimensions
import com.tneagu.realestateapp.core.ui.theme.RealEstateAppTheme
import com.tneagu.realestateapp.core.ui.theme.Spacing

/**
 * Reusable tag/chip component for displaying metadata.
 * Used for property type, offer type, and other categorical information.
 *
 * @param text The text to display in the tag.
 * @param modifier Modifier to be applied to the tag.
 * @param containerColor Background color of the tag. Defaults to secondaryContainer.
 * @param contentColor Text color of the tag. Defaults to onSecondaryContainer.
 */
@Composable
fun PropertyTag(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = containerColor,
        tonalElevation = Dimensions.cardElevation
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
            modifier = Modifier.padding(
                horizontal = Spacing.small,
                vertical = Spacing.extraSmall
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PropertyTagPreview() {
    RealEstateAppTheme {
        PropertyTag(text = "Apartment")
    }
}

@Preview(showBackground = true)
@Composable
private fun PropertyTagCustomColorPreview() {
    RealEstateAppTheme {
        PropertyTag(
            text = "FOR RENT",
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
