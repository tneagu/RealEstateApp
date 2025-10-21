package com.tneagu.realestateapp.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tneagu.realestateapp.core.ui.theme.RealEstateAppTheme
import com.tneagu.realestateapp.core.ui.theme.Spacing

/**
 * Shared loading view component.
 * Displays a centered circular progress indicator with an optional message.
 *
 * @param modifier Modifier to be applied to the root element.
 * @param message Optional loading message to display below the indicator.
 */
@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    message: String? = null
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )

        if (message != null) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = Spacing.default)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingViewPreview() {
    RealEstateAppTheme {
        LoadingView()
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingViewWithMessagePreview() {
    RealEstateAppTheme {
        LoadingView(message = "Loading listings...")
    }
}
