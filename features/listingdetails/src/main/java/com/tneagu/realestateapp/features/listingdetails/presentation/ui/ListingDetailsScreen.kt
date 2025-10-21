package com.tneagu.realestateapp.features.listingdetails.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.tneagu.realestateapp.core.ui.components.ErrorView
import com.tneagu.realestateapp.core.ui.components.LoadingView
import com.tneagu.realestateapp.features.listingdetails.R
import com.tneagu.realestateapp.features.listingdetails.presentation.mvi.ListingDetailsEffect
import com.tneagu.realestateapp.features.listingdetails.presentation.mvi.ListingDetailsIntent
import com.tneagu.realestateapp.features.listingdetails.presentation.mvi.ListingDetailsState
import com.tneagu.realestateapp.features.listingdetails.presentation.ui.components.ListingDetailsContent
import com.tneagu.realestateapp.features.listingdetails.presentation.ui.preview.sampleListingDetailMinimal
import com.tneagu.realestateapp.features.listingdetails.presentation.ui.preview.sampleListingDetailRent
import com.tneagu.realestateapp.features.listingdetails.presentation.ui.preview.sampleListingDetailSale
import com.tneagu.realestateapp.features.listingdetails.presentation.viewmodel.ListingDetailsViewModel
import com.tneagu.realestateapp.core.ui.R as CoreUiR

/**
 * Main screen for displaying detailed information about a property listing.
 *
 * @param listingId The unique identifier of the listing to display.
 * @param onNavigateBack Callback invoked when the user navigates back.
 * @param viewModel The ViewModel managing the screen state.
 * @param modifier Optional modifier for the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingDetailsScreen(
    listingId: Int,
    onNavigateBack: () -> Unit,
    viewModel: ListingDetailsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Handle one-time effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ListingDetailsEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    // Load details on first composition
    LaunchedEffect(listingId) {
        viewModel.handleIntent(ListingDetailsIntent.LoadDetails(listingId))
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.listing_details_title)) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.handleIntent(ListingDetailsIntent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.listing_details_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state) {
                ListingDetailsState.NotInitialized -> {
                    // Show nothing or placeholder
                }

                ListingDetailsState.Loading -> {
                    LoadingView(message = stringResource(R.string.listing_details_loading))
                }

                is ListingDetailsState.Success -> {
                    ListingDetailsContent(
                        detail = (state as ListingDetailsState.Success).detail
                    )
                }

                is ListingDetailsState.Error -> {
                    ErrorView(
                        message = getErrorMessage((state as ListingDetailsState.Error).error),
                        onRetry = { viewModel.handleIntent(ListingDetailsIntent.Retry) }
                    )
                }
            }
        }
    }
}

@Composable
private fun getErrorMessage(error: DomainError): String {
    return when (error) {
        is DomainError.NetworkUnavailable ->
            error.message ?: stringResource(CoreUiR.string.error_network)

        is DomainError.ServerError ->
            error.message ?: stringResource(CoreUiR.string.error_server)

        is DomainError.UnknownError ->
            error.message ?: stringResource(CoreUiR.string.error_unknown)
    }
}

/**
 * Stateless version of ListingDetailsScreen for previewing different states.
 * Does not require ViewModel, making it suitable for Compose previews.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListingDetailsScreenPreview(
    state: ListingDetailsState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.listing_details_title)) },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.listing_details_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state) {
                ListingDetailsState.NotInitialized -> {
                    // Show nothing
                }

                ListingDetailsState.Loading -> {
                    LoadingView(message = stringResource(R.string.listing_details_loading))
                }

                is ListingDetailsState.Success -> {
                    ListingDetailsContent(detail = state.detail)
                }

                is ListingDetailsState.Error -> {
                    ErrorView(
                        message = when (state.error) {
                            is DomainError.NetworkUnavailable -> "No network connection"
                            is DomainError.ServerError -> "Server error"
                            is DomainError.UnknownError -> "Unknown error"
                        },
                        onRetry = { }
                    )
                }
            }
        }
    }
}

@Preview(name = "Screen - Loading", showBackground = true)
@Composable
private fun ListingDetailsScreenLoadingPreview() {
    com.tneagu.realestateapp.core.ui.theme.RealEstateAppTheme {
        ListingDetailsScreenPreview(state = ListingDetailsState.Loading)
    }
}

@Preview(name = "Screen - Success Rent", showBackground = true)
@Composable
private fun ListingDetailsScreenSuccessRentPreview() {
    com.tneagu.realestateapp.core.ui.theme.RealEstateAppTheme {
        ListingDetailsScreenPreview(
            state = ListingDetailsState.Success(sampleListingDetailRent)
        )
    }
}

@Preview(name = "Screen - Success Sale", showBackground = true)
@Composable
private fun ListingDetailsScreenSuccessSalePreview() {
    com.tneagu.realestateapp.core.ui.theme.RealEstateAppTheme {
        ListingDetailsScreenPreview(
            state = ListingDetailsState.Success(sampleListingDetailSale)
        )
    }
}

@Preview(name = "Screen - Success Minimal", showBackground = true)
@Composable
private fun ListingDetailsScreenSuccessMinimalPreview() {
    com.tneagu.realestateapp.core.ui.theme.RealEstateAppTheme {
        ListingDetailsScreenPreview(
            state = ListingDetailsState.Success(sampleListingDetailMinimal)
        )
    }
}

@Preview(name = "Screen - Error Server", showBackground = true)
@Composable
private fun ListingDetailsScreenErrorServerPreview() {
    com.tneagu.realestateapp.core.ui.theme.RealEstateAppTheme {
        ListingDetailsScreenPreview(
            state = ListingDetailsState.Error(
                DomainError.ServerError("Server returned 500 error")
            )
        )
    }
}
