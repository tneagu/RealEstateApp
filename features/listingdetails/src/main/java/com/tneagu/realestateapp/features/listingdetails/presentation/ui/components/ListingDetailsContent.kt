package com.tneagu.realestateapp.features.listingdetails.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tneagu.realestateapp.core.ui.theme.RealEstateAppTheme
import com.tneagu.realestateapp.features.listingdetails.R
import com.tneagu.realestateapp.features.listingdetails.domain.model.ListingDetail
import com.tneagu.realestateapp.features.listingdetails.presentation.ui.preview.sampleListingDetailMinimal
import com.tneagu.realestateapp.features.listingdetails.presentation.ui.preview.sampleListingDetailRent
import com.tneagu.realestateapp.features.listingdetails.presentation.ui.preview.sampleListingDetailSale

/**
 * Displays the full listing details content with hero image, property information, and agent details.
 *
 * @param detail The listing detail to display.
 * @param modifier Optional modifier for the content.
 */
@Composable
internal fun ListingDetailsContent(
    detail: ListingDetail,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Image
        AsyncImage(
            model = detail.imageUrl,
            contentDescription = detail.propertyType,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        // Property Information
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Property Header (title, price, offer type badge)
            PropertyHeader(
                propertyType = detail.propertyType,
                city = detail.city,
                price = detail.price,
                offerType = detail.offerType
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Property Details Section
            PropertyDetailsSection(
                bedrooms = detail.bedrooms,
                rooms = detail.rooms,
                area = detail.area
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Agent Section
            Text(
                text = stringResource(R.string.listing_details_section_agent),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = detail.professional,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

// ========================================
// Previews
// ========================================

@Preview(name = "Listing Details Content - Rent", showBackground = true)
@Composable
private fun ListingDetailsContentRentPreview() {
    RealEstateAppTheme {
        ListingDetailsContent(detail = sampleListingDetailRent)
    }
}

@Preview(name = "Listing Details Content - Sale", showBackground = true)
@Composable
private fun ListingDetailsContentSalePreview() {
    RealEstateAppTheme {
        ListingDetailsContent(detail = sampleListingDetailSale)
    }
}

@Preview(name = "Listing Details Content - Minimal Data", showBackground = true)
@Composable
private fun ListingDetailsContentMinimalPreview() {
    RealEstateAppTheme {
        ListingDetailsContent(detail = sampleListingDetailMinimal)
    }
}
