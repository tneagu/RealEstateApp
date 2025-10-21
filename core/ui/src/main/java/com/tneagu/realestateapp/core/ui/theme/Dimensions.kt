package com.tneagu.realestateapp.core.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Centralized dimension constants for UI components.
 * Ensures consistent sizing across the application.
 */
object Dimensions {
    // Card dimensions
    /** Standard elevation for cards */
    val cardElevation = 2.dp

    /** Corner radius for cards and rounded elements */
    val cardCornerRadius = 12.dp

    // Image dimensions
    /** Height for listing images in cards */
    val listingImageHeight = 200.dp

    /** Aspect ratio for listing images (width / height) */
    const val listingImageAspectRatio = 16f / 9f

    /** Height for detail screen hero image */
    val detailImageHeight = 300.dp

    // Icon sizes
    val iconSmall = 16.dp
    val iconMedium = 24.dp
    val iconLarge = 32.dp
    val iconExtraLarge = 48.dp

    // Touch targets & component heights
    /** Minimum touch target size (accessibility)*/
    val minTouchTarget = 48.dp

    /** Standard button height*/
    val buttonHeight = 48.dp

    /** Top app bar height */
    val topAppBarHeight = 64.dp

    // Dividers & borders
    /** Standard divider thickness */
    val dividerThickness = 1.dp

    /** Border width for outlined elements */
    val borderWidth = 1.dp
}
