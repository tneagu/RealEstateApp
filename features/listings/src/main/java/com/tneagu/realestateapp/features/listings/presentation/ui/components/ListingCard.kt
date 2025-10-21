package com.tneagu.realestateapp.features.listings.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import java.util.Locale
import com.tneagu.realestateapp.core.ui.components.PropertyTag
import com.tneagu.realestateapp.core.ui.theme.Dimensions
import com.tneagu.realestateapp.core.ui.theme.RealEstateAppTheme
import com.tneagu.realestateapp.core.ui.theme.Spacing
import com.tneagu.realestateapp.features.listings.domain.model.Listing
import com.tneagu.realestateapp.core.domain.model.OfferType

/**
 * Card component for displaying a real estate listing.
 *
 * @param listing The listing data to display.
 * @param onClick Callback invoked when the card is clicked.
 * @param modifier Modifier to be applied to the card.
 */
@Composable
fun ListingCard(
    listing: Listing,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onClick(listing.id) },
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.cardElevation)
    ) {
        Column {
            // Property Image
            AsyncImage(
                model = listing.imageUrl ?: "",
                contentDescription = "Property image for ${listing.propertyType} in ${listing.city}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(Dimensions.listingImageAspectRatio)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            // Content Section
            Column(
                modifier = Modifier.padding(Spacing.default)
            ) {
                // Property Type Tag & Offer Type Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PropertyTag(
                        text = listing.propertyType,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    // Offer Type Badge
                    val (badgeText, badgeColor, badgeContentColor) = when (listing.offerType) {
                        OfferType.RENT -> Triple(
                            "FOR RENT",
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        OfferType.SALE -> Triple(
                            "FOR SALE",
                            MaterialTheme.colorScheme.tertiaryContainer,
                            MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        OfferType.UNKNOWN -> Triple(
                            "UNKNOWN",
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    PropertyTag(
                        text = badgeText,
                        containerColor = badgeColor,
                        contentColor = badgeContentColor
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.small))

                // Price
                Text(
                    text = "€${String.format(Locale.getDefault(), "%,.0f", listing.price)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(Spacing.extraSmall))

                // Location
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(Dimensions.iconSmall)
                    )
                    Spacer(modifier = Modifier.width(Spacing.extraSmall))
                    Text(
                        text = listing.city,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.small))

                // Details Row (Bedrooms, Rooms, Area)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.medium)
                ) {
                    // Bedrooms
                    if (listing.bedrooms != null) {
                        PropertyDetail(
                            icon = Icons.Default.Bed,
                            value = "${listing.bedrooms} bed${if (listing.bedrooms > 1) "s" else ""}"
                        )
                    }

                    // Rooms
                    if (listing.rooms != null) {
                        PropertyDetail(
                            icon = Icons.Default.MeetingRoom,
                            value = "${listing.rooms} room${if (listing.rooms > 1) "s" else ""}"
                        )
                    }

                    // Area
                    PropertyDetail(
                        icon = Icons.Default.SquareFoot,
                        value = "${String.format(Locale.getDefault(), "%.1f", listing.area)} m²"
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.small))

                // Professional Name
                Text(
                    text = listing.professional,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Helper composable for displaying property details with an icon.
 */
@Composable
private fun PropertyDetail(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(Dimensions.iconSmall)
        )
        Spacer(modifier = Modifier.width(Spacing.extraSmall))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ListingCardPreview() {
    RealEstateAppTheme {
        ListingCard(
            listing = Listing(
                id = 1,
                bedrooms = 3,
                city = "Paris",
                area = 120.5,
                imageUrl = null,
                price = 450000.0,
                professional = "Real Estate Pro",
                propertyType = "Apartment",
                offerType = OfferType.RENT,
                rooms = 5
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ListingCardForSalePreview() {
    RealEstateAppTheme {
        ListingCard(
            listing = Listing(
                id = 2,
                bedrooms = 4,
                city = "Lyon",
                area = 200.0,
                imageUrl = null,
                price = 850000.0,
                professional = "Premium Properties",
                propertyType = "House",
                offerType = OfferType.SALE,
                rooms = 7
            ),
            onClick = {}
        )
    }
}
