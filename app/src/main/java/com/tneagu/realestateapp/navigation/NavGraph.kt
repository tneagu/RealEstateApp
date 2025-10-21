package com.tneagu.realestateapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tneagu.realestateapp.features.listingdetails.presentation.ui.ListingDetailsScreen
import com.tneagu.realestateapp.features.listings.presentation.ui.ListingsScreen

/**
 * Represents navigation routes in the app.
 * Each route defines its path and any required arguments.
 */
sealed interface Route {
    val route: String

    /**
     * Route for the listings screen.
     */
    data object Listings : Route {
        override val route = "listings"
    }

    /**
     * Route for the listing details screen.
     * Requires a listing ID argument.
     */
    data object ListingDetails : Route {
        override val route = "listing_details"
        const val ARG_LISTING_ID = "listingId"

        /**
         * Creates a route string with the provided listing ID.
         *
         * @param listingId The ID of the listing to display.
         * @return The complete route string.
         */
        fun createRoute(listingId: Int) = "$route/$listingId"

        /**
         * Route pattern with argument placeholder for navigation graph.
         */
        val routeWithArgs = "$route/{$ARG_LISTING_ID}"
    }
}

/**
 * Main navigation host for the app.
 * Defines all navigation routes and their corresponding screens.
 *
 * @param modifier Modifier to be applied to the NavHost.
 * @param navController The navigation controller that manages app navigation.
 * @param startDestination The route to display when the app launches.
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Route.Listings.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Listings screen - shows list of properties
        composable(route = Route.Listings.route) {
            ListingsScreen(
                onListingClick = { listingId ->
                    navController.navigate(Route.ListingDetails.createRoute(listingId))
                }
            )
        }

        // Listing details screen - shows details for a specific property
        composable(
            route = Route.ListingDetails.routeWithArgs,
            arguments = listOf(
                navArgument(Route.ListingDetails.ARG_LISTING_ID) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val listingId = backStackEntry.arguments?.getInt(Route.ListingDetails.ARG_LISTING_ID) ?: 0
            ListingDetailsScreen(
                listingId = listingId,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
