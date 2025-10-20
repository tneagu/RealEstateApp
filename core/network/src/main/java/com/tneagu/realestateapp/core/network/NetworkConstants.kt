package com.tneagu.realestateapp.core.network

/**
 * Network-related constants for API configuration.
 */
object NetworkConstants {
    const val BASE_URL = "https://gsl-apps-technical-test.dignp.com/"

    // Endpoints
    const val LISTINGS_ENDPOINT = "listings.json"
    const val LISTING_DETAILS_ENDPOINT = "listings/{listingId}.json"

    // Timeout configurations (in seconds)
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L
}
