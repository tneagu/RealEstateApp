package com.tneagu.realestateapp.core.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Consistent spacing system following 8dp grid.
 * Use these values throughout the app for consistent padding, margins, and gaps.
 */
object Spacing {
    /** No spacing - 0dp */
    val none = 0.dp

    /** Extra small spacing - 4dp. Use for: tight spacing, icon padding */
    val extraSmall = 4.dp

    /** Small spacing - 8dp. Use for: compact spacing, small gaps between elements */
    val small = 8.dp

    /** Medium spacing - 12dp. Use for: medium gaps, compact card padding */
    val medium = 12.dp

    /** Default spacing - 16dp. Use for: standard padding (cards, screens), default margins */
    val default = 16.dp

    /** Large spacing - 24dp. Use for: section spacing, gaps between major elements */
    val large = 24.dp

    /** Extra large spacing - 32dp. Use for: major sections, screen edges, top/bottom margins */
    val extraLarge = 32.dp

    /** Huge spacing - 48dp. Use for: very large separations (rarely used) */
    val huge = 48.dp
}
