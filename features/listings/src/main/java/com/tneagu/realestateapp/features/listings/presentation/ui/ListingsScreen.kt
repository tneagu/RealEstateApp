package com.tneagu.realestateapp.features.listings.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tneagu.realestateapp.core.domain.model.DomainError
import com.tneagu.realestateapp.core.domain.model.OfferType
import com.tneagu.realestateapp.core.ui.components.EmptyView
import com.tneagu.realestateapp.core.ui.components.ErrorView
import com.tneagu.realestateapp.core.ui.components.LoadingView
import com.tneagu.realestateapp.core.ui.theme.RealEstateAppTheme
import com.tneagu.realestateapp.core.ui.theme.Spacing
import com.tneagu.realestateapp.features.listings.R
import com.tneagu.realestateapp.features.listings.domain.model.Listing
import com.tneagu.realestateapp.features.listings.presentation.mvi.ListingsEffect
import com.tneagu.realestateapp.features.listings.presentation.mvi.ListingsIntent
import com.tneagu.realestateapp.features.listings.presentation.mvi.ListingsState
import com.tneagu.realestateapp.features.listings.presentation.ui.components.ListingCard
import com.tneagu.realestateapp.features.listings.presentation.viewmodel.ListingsViewModel
import com.tneagu.realestateapp.core.ui.R as CoreUiR

/**
 * Main listings screen that displays a list of real estate listings.
 *
 * @param navigateToListingDetails Callback invoked when navigating to listing details.
 * @param viewModel The ViewModel that manages the screen state.
 * @param modifier Modifier to be applied to the root element.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingsScreen(
    navigateToListingDetails: (Int) -> Unit,
    viewModel: ListingsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Trigger initial load when screen is first displayed
    LaunchedEffect(Unit) {
        if (state is ListingsState.NotInitialized) {
            viewModel.handleIntent(ListingsIntent.LoadListings)
        }
    }

    // Collect effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ListingsEffect.NavigateToDetails -> {
                    navigateToListingDetails(effect.listingId)
                }
                is ListingsEffect.ShowError -> {
                    // Handle error effect if needed
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.listings_screen_title),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
            )
        },
    ) { paddingValues ->
        when (val currentState = state) {
            is ListingsState.NotInitialized -> {
                // Show nothing, loading will be triggered by LaunchedEffect
            }

            is ListingsState.Loading -> {
                LoadingView(
                    modifier = Modifier.padding(paddingValues),
                    message = stringResource(R.string.listings_loading),
                )
            }

            is ListingsState.Success -> {
                if (currentState.listings.isEmpty()) {
                    EmptyView(
                        message = stringResource(R.string.listings_empty),
                        modifier = Modifier.padding(paddingValues),
                    )
                } else {
                    ListingsContent(
                        listings = currentState.listings,
                        onListingClick = { listingId ->
                            viewModel.handleIntent(ListingsIntent.OnListingClick(listingId))
                        },
                        modifier = Modifier.padding(paddingValues),
                    )
                }
            }

            is ListingsState.Error -> {
                ErrorView(
                    message = getErrorMessage(currentState.error),
                    onRetry = { viewModel.handleIntent(ListingsIntent.Retry) },
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }
    }
}

/**
 * Displays the list of listings in a lazy column.
 */
@Composable
private fun ListingsContent(
    listings: List<Listing>,
    onListingClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(Spacing.default),
        verticalArrangement = Arrangement.spacedBy(Spacing.default),
    ) {
        items(
            items = listings,
            key = { it.id },
        ) { listing ->
            ListingCard(
                listing = listing,
                onClick = onListingClick,
            )
        }
    }
}

/**
 * Converts domain error to user-friendly message.
 */
@Composable
private fun getErrorMessage(error: DomainError): String {
    return when (error) {
        is DomainError.NetworkUnavailable -> {
            error.message ?: stringResource(CoreUiR.string.error_network)
        }
        is DomainError.ServerError -> {
            error.message ?: stringResource(CoreUiR.string.error_server)
        }
        is DomainError.UnknownError -> {
            error.message ?: stringResource(CoreUiR.string.error_unknown)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun ListingsScreenLoadingPreview() {
    RealEstateAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Real Estate Listings") },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                )
            },
        ) { paddingValues ->
            LoadingView(
                modifier = Modifier.padding(paddingValues),
                message = "Loading listings...",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListingsContentPreview() {
    RealEstateAppTheme {
        ListingsContent(
            listings =
                listOf(
                    Listing(
                        id = 1,
                        bedrooms = 3,
                        city = "Paris",
                        area = 120.5,
                        imageUrl = null,
                        price = 450000.0,
                        professional = "Real Estate Pro",
                        propertyType = "Apartment",
                        offerType = OfferType.RENT,
                        rooms = 5,
                    ),
                    Listing(
                        id = 2,
                        bedrooms = 4,
                        city = "Lyon",
                        area = 200.0,
                        imageUrl = null,
                        price = 850000.0,
                        professional = "Premium Properties",
                        propertyType = "House",
                        offerType = OfferType.SALE,
                        rooms = 7,
                    ),
                ),
            onListingClick = {},
        )
    }
}
