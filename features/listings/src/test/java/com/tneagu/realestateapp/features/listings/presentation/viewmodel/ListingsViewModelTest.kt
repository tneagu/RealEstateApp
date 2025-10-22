package com.tneagu.realestateapp.features.listings.presentation.viewmodel

import app.cash.turbine.test
import com.tneagu.realestateapp.core.domain.model.DataResult
import com.tneagu.realestateapp.core.domain.model.DomainError
import com.tneagu.realestateapp.core.domain.model.OfferType
import com.tneagu.realestateapp.features.listings.domain.model.Listing
import com.tneagu.realestateapp.features.listings.domain.usecase.GetListingsUseCase
import com.tneagu.realestateapp.features.listings.presentation.mvi.ListingsEffect
import com.tneagu.realestateapp.features.listings.presentation.mvi.ListingsIntent
import com.tneagu.realestateapp.features.listings.presentation.mvi.ListingsState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListingsViewModelTest {

    private lateinit var classUnderTest: ListingsViewModel
    private val getListingsUseCase = mockk<GetListingsUseCase>()

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        classUnderTest = ListingsViewModel(getListingsUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Test Data
    private val sampleListings = listOf(
        Listing(
            id = 1,
            bedrooms = 3,
            city = "Paris",
            area = 120.5,
            imageUrl = "https://example.com/image1.jpg",
            price = 450000.0,
            professional = "Real Estate Pro",
            propertyType = "Apartment",
            offerType = OfferType.RENT,
            rooms = 5
        ),
        Listing(
            id = 2,
            bedrooms = 2,
            city = "Lyon",
            area = 80.0,
            imageUrl = "https://example.com/image2.jpg",
            price = 300000.0,
            professional = "Property Expert",
            propertyType = "House",
            offerType = OfferType.SALE,
            rooms = 4
        )
    )

    // ========== Initialization Tests ==========

    @Test
    fun `initial state is NotInitialized`() = runTest {
        // When
        classUnderTest.state.test {
            // Then
            assertEquals(ListingsState.NotInitialized, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ========== LoadListings Intent Tests ==========

    @Test
    fun `handleIntent LoadListings emits Loading then Success state`() = runTest {
        // Given
        val successResult = DataResult.Success(sampleListings)
        coEvery { getListingsUseCase() } returns successResult

        // When
        classUnderTest.state.test {
            // Skip initial state
            assertEquals(ListingsState.NotInitialized, awaitItem())

            classUnderTest.handleIntent(ListingsIntent.LoadListings)
            advanceUntilIdle()

            // Then
            assertEquals(ListingsState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is ListingsState.Success)
            assertEquals(sampleListings, (successState as ListingsState.Success).listings)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { getListingsUseCase() }
    }

    @Test
    fun `handleIntent LoadListings emits Loading then Error on NetworkUnavailable`() = runTest {
        // Given
        val networkError = DomainError.NetworkUnavailable("No internet connection")
        val errorResult = DataResult.Failure(networkError)
        coEvery { getListingsUseCase() } returns errorResult

        // When
        classUnderTest.state.test {
            // Skip initial state
            assertEquals(ListingsState.NotInitialized, awaitItem())

            classUnderTest.handleIntent(ListingsIntent.LoadListings)
            advanceUntilIdle()

            // Then
            assertEquals(ListingsState.Loading, awaitItem())
            val errorState = awaitItem()
            assertTrue(errorState is ListingsState.Error)
            assertEquals(networkError, (errorState as ListingsState.Error).error)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { getListingsUseCase() }
    }

    @Test
    fun `handleIntent LoadListings emits Loading then Error on ServerError`() = runTest {
        // Given
        val serverError = DomainError.ServerError("Internal server error")
        val errorResult = DataResult.Failure(serverError)
        coEvery { getListingsUseCase() } returns errorResult

        // When
        classUnderTest.state.test {
            // Skip initial state
            assertEquals(ListingsState.NotInitialized, awaitItem())

            classUnderTest.handleIntent(ListingsIntent.LoadListings)
            advanceUntilIdle()

            // Then
            assertEquals(ListingsState.Loading, awaitItem())
            val errorState = awaitItem()
            assertTrue(errorState is ListingsState.Error)
            assertEquals(serverError, (errorState as ListingsState.Error).error)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { getListingsUseCase() }
    }

    @Test
    fun `handleIntent LoadListings emits Loading then Error on UnknownError`() = runTest {
        // Given
        val unknownError = DomainError.UnknownError("Something went wrong")
        val errorResult = DataResult.Failure(unknownError)
        coEvery { getListingsUseCase() } returns errorResult

        // When
        classUnderTest.state.test {
            // Skip initial state
            assertEquals(ListingsState.NotInitialized, awaitItem())

            classUnderTest.handleIntent(ListingsIntent.LoadListings)
            advanceUntilIdle()

            // Then
            assertEquals(ListingsState.Loading, awaitItem())
            val errorState = awaitItem()
            assertTrue(errorState is ListingsState.Error)
            assertEquals(unknownError, (errorState as ListingsState.Error).error)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { getListingsUseCase() }
    }

    // ========== Retry Intent Tests ==========

    @Test
    fun `handleIntent Retry triggers loading flow from error state`() = runTest {
        // Given - first call fails, second succeeds
        val errorResult = DataResult.Failure(DomainError.NetworkUnavailable())
        val successResult = DataResult.Success(sampleListings)
        coEvery { getListingsUseCase() } returnsMany listOf(errorResult, successResult)

        // When
        classUnderTest.state.test {
            // Skip initial state
            assertEquals(ListingsState.NotInitialized, awaitItem())

            // First attempt fails
            classUnderTest.handleIntent(ListingsIntent.LoadListings)
            advanceUntilIdle()
            assertEquals(ListingsState.Loading, awaitItem())
            assertTrue(awaitItem() is ListingsState.Error)

            // Retry succeeds
            classUnderTest.handleIntent(ListingsIntent.Retry)
            advanceUntilIdle()

            // Then
            assertEquals(ListingsState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is ListingsState.Success)
            assertEquals(sampleListings, (successState as ListingsState.Success).listings)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 2) { getListingsUseCase() }
    }

    // ========== OnListingClick Intent Tests ==========

    @Test
    fun `handleIntent OnListingClick emits NavigateToDetails effect with correct listingId`() =
        runTest {
            // Given
            val listingId = 42

            // When
            classUnderTest.effect.test {
                classUnderTest.handleIntent(ListingsIntent.OnListingClick(listingId))
                advanceUntilIdle()

                // Then
                val effect = awaitItem()
                assertTrue(effect is ListingsEffect.NavigateToDetails)
                assertEquals(listingId, (effect as ListingsEffect.NavigateToDetails).listingId)

                cancelAndIgnoreRemainingEvents()
            }
        }
}
