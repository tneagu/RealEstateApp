package com.tneagu.realestateapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tneagu.realestateapp.core.ui.theme.RealEstateAppTheme
import com.tneagu.realestateapp.features.listings.presentation.ui.ListingsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RealEstateAppTheme {
                ListingsScreen(
                    onListingClick = { listingId ->
                        // TODO: Navigate to details screen with listingId
                        // This will be implemented in Phase 6+
                    }
                )
            }
        }
    }
}