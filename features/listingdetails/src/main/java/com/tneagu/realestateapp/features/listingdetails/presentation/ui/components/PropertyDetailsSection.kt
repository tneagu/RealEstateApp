package com.tneagu.realestateapp.features.listingdetails.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tneagu.realestateapp.core.ui.theme.RealEstateAppTheme
import com.tneagu.realestateapp.features.listingdetails.R

/**
 * Displays the property details section with bedrooms, rooms, and area information.
 *
 * @param bedrooms The number of bedrooms (nullable).
 * @param rooms The total number of rooms (nullable).
 * @param area The area in square meters.
 * @param modifier Optional modifier for the section.
 */
@Composable
fun PropertyDetailsSection(
    bedrooms: Int?,
    rooms: Int?,
    area: Double,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Section Title
        Text(
            text = stringResource(R.string.listing_details_section_details),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Property Details Grid
        PropertyDetailRow(label = "Bedrooms", value = bedrooms?.toString() ?: "N/A")
        PropertyDetailRow(label = "Rooms", value = rooms?.toString() ?: "N/A")
        PropertyDetailRow(label = "Area", value = "$area m²")
    }
}

@Preview(name = "Property Details Section - Full Data", showBackground = true)
@Composable
private fun PropertyDetailsSectionPreview() {
    RealEstateAppTheme {
        PropertyDetailsSection(
            bedrooms = 3,
            rooms = 4,
            area = 85.5,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "Property Details Section - Minimal Data", showBackground = true)
@Composable
private fun PropertyDetailsSectionMinimalPreview() {
    RealEstateAppTheme {
        PropertyDetailsSection(
            bedrooms = null,
            rooms = null,
            area = 60.0,
            modifier = Modifier.padding(16.dp)
        )
    }
}
