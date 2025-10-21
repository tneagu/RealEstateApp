package com.tneagu.realestateapp.features.listingdetails.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tneagu.realestateapp.core.ui.theme.RealEstateAppTheme

/**
 * Displays a single property detail as a label-value row.
 *
 * @param label The label for the property detail (e.g., "Bedrooms").
 * @param value The value for the property detail (e.g., "3" or "N/A").
 * @param modifier Optional modifier for the row.
 */
@Composable
fun PropertyDetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(name = "Property Detail Row - With Value", showBackground = true)
@Composable
private fun PropertyDetailRowPreview() {
    RealEstateAppTheme {
        PropertyDetailRow(label = "Bedrooms", value = "3")
    }
}

@Preview(name = "Property Detail Row - N/A", showBackground = true)
@Composable
private fun PropertyDetailRowNAPreview() {
    RealEstateAppTheme {
        PropertyDetailRow(label = "Rooms", value = "N/A")
    }
}
