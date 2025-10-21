package com.tneagu.realestateapp.features.listingdetails.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tneagu.realestateapp.core.domain.model.OfferType
import com.tneagu.realestateapp.core.ui.theme.RealEstateAppTheme

/**
 * Displays the property header with title, price, and offer type badge.
 *
 * @param propertyType The type of property (e.g., "Apartment", "House").
 * @param city The city where the property is located.
 * @param price The price of the property.
 * @param offerType The type of offer (RENT or SALE).
 * @param modifier Optional modifier for the header.
 */
@Composable
fun PropertyHeader(
    propertyType: String,
    city: String,
    price: Double,
    offerType: OfferType,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Title
        Text(
            text = "$propertyType in $city",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Price and Offer Type
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "€$price",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Surface(
                color = if (offerType == OfferType.RENT) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.tertiaryContainer
                },
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = offerType.name,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(name = "Property Header - Rent", showBackground = true)
@Composable
private fun PropertyHeaderRentPreview() {
    RealEstateAppTheme {
        PropertyHeader(
            propertyType = "Apartment",
            city = "Paris",
            price = 1500.0,
            offerType = OfferType.RENT,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "Property Header - Sale", showBackground = true)
@Composable
private fun PropertyHeaderSalePreview() {
    RealEstateAppTheme {
        PropertyHeader(
            propertyType = "House",
            city = "Lyon",
            price = 350000.0,
            offerType = OfferType.SALE,
            modifier = Modifier.padding(16.dp)
        )
    }
}
