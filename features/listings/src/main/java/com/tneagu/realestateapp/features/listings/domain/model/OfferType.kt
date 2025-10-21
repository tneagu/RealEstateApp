package com.tneagu.realestateapp.features.listings.domain.model

/**
 * Represents the type of real estate offer.
 */
enum class OfferType {
    RENT,
    SALE,
    UNKNOWN;

    companion object {
        /**
         * Converts an integer value to an [OfferType].
         *
         * @param value The integer value from the API (1 = RENT, 2 = SALE).
         * @return The corresponding [OfferType], or [UNKNOWN] for unrecognized values.
         */
        fun fromInt(value: Int): OfferType {
            return when (value) {
                1 -> RENT
                2 -> SALE
                else -> UNKNOWN
            }
        }
    }
}
