package com.tneagu.realestateapp.features.listingdetails.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tneagu.realestateapp.core.domain.model.DomainError
import com.tneagu.realestateapp.core.ui.components.ErrorView
import com.tneagu.realestateapp.core.ui.components.LoadingView
import com.tneagu.realestateapp.features.listingdetails.R
import com.tneagu.realestateapp.features.listingdetails.domain.model.ListingDetail
import com.tneagu.realestateapp.features.listingdetails.presentation.mvi.ListingDetailsEffect
import com.tneagu.realestateapp.features.listingdetails.presentation.mvi.ListingDetailsIntent
import com.tneagu.realestateapp.features.listingdetails.presentation.mvi.ListingDetailsState
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

@Composable
private fun ListingDetailsContent(
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
            // Title
            Text(
                text = "${detail.propertyType} in ${detail.city}",
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
                    text = "€${detail.price}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    color = if (detail.offerType == com.tneagu.realestateapp.core.domain.model.OfferType.RENT) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.tertiaryContainer
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = detail.offerType.name,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Property Details Section
            Text(
                text = stringResource(R.string.listing_details_section_details),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Property Details Grid
            PropertyDetailRow(label = "Bedrooms", value = detail.bedrooms?.toString() ?: "N/A")
            PropertyDetailRow(label = "Rooms", value = detail.rooms?.toString() ?: "N/A")
            PropertyDetailRow(label = "Area", value = "${detail.area} m²")

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

@Composable
private fun PropertyDetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
