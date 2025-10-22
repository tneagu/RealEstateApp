package com.tneagu.realestateapp.features.listingdetails.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.tneagu.realestateapp.core.domain.model.DataResult
import com.tneagu.realestateapp.core.domain.model.DomainError
import com.tneagu.realestateapp.core.domain.model.OfferType
import com.tneagu.realestateapp.features.listingdetails.domain.model.ListingDetail
import com.tneagu.realestateapp.features.listingdetails.domain.usecase.GetListingDetailsUseCase
import com.tneagu.realestateapp.features.listingdetails.presentation.mvi.ListingDetailsEffect
import com.tneagu.realestateapp.features.listingdetails.presentation.mvi.ListingDetailsIntent
import com.tneagu.realestateapp.features.listingdetails.presentation.mvi.ListingDetailsState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
import org.junit.jupiter.api.assertThrows

@OptIn(ExperimentalCoroutinesApi::class)
class ListingDetailsViewModelTest {
    private lateinit var classUnderTest: ListingDetailsViewModel
    private val getListingDetailsUseCase = mockk<GetListingDetailsUseCase>()
    private val savedStateHandle = mockk<SavedStateHandle>()

    private val testDispatcher = StandardTestDispatcher()

    private val testListingId = 42

    // Test Data
    private val sampleListingDetail =
        ListingDetail(
            id = testListingId,
            bedrooms = 3,
            city = "Paris",
            area = 120.5,
            imageUrl = "https://example.com/image.jpg",
            price = 450000.0,
            professional = "Real Estate Pro",
            propertyType = "Apartment",
            offerType = OfferType.RENT,
            rooms = 5,
        )

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Default: savedStateHandle returns valid listingId
        every { savedStateHandle.get<Int>("listingId") } returns testListingId

        classUnderTest = ListingDetailsViewModel(getListingDetailsUseCase, savedStateHandle)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ========== Initialization Tests ==========

    @Test
    fun `initial state is NotInitialized`() =
        runTest {
            // When
            classUnderTest.state.test {
                // Then
                assertEquals(ListingDetailsState.NotInitialized, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `ViewModel retrieves listingId from SavedStateHandle`() =
        runTest {
            verify { savedStateHandle.get<Int>("listingId") }
        }

    @Test
    fun `ViewModel throws IllegalStateException when listingId is missing`() {
        // Given
        every { savedStateHandle.get<Int>("listingId") } returns null

        // When & Then
        val exception =
            assertThrows<IllegalStateException> {
                ListingDetailsViewModel(getListingDetailsUseCase, savedStateHandle)
            }

        assertTrue(exception.message?.contains("ListingDetailsViewModel requires a listingId argument") == true)
    }

    // ========== LoadDetails Intent Tests ==========

    @Test
    fun `handleIntent LoadDetails emits Loading then Success state`() =
        runTest {
            // Given
            val successResult = DataResult.Success(sampleListingDetail)
            coEvery { getListingDetailsUseCase(testListingId) } returns successResult

            // When
            classUnderTest.state.test {
                // Skip initial state
                assertEquals(ListingDetailsState.NotInitialized, awaitItem())

                classUnderTest.handleIntent(ListingDetailsIntent.LoadDetails(testListingId))
                advanceUntilIdle()

                // Then
                assertEquals(ListingDetailsState.Loading, awaitItem())
                val successState = awaitItem()
                assertTrue(successState is ListingDetailsState.Success)
                assertEquals(sampleListingDetail, (successState as ListingDetailsState.Success).detail)

                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) { getListingDetailsUseCase(testListingId) }
        }

    @Test
    fun `handleIntent LoadDetails uses correct listingId from intent`() =
        runTest {
            // Given
            val customListingId = 999
            val customDetail = sampleListingDetail.copy(id = customListingId)
            val successResult = DataResult.Success(customDetail)
            coEvery { getListingDetailsUseCase(customListingId) } returns successResult

            // When
            classUnderTest.state.test {
                // Skip initial state
                assertEquals(ListingDetailsState.NotInitialized, awaitItem())

                classUnderTest.handleIntent(ListingDetailsIntent.LoadDetails(customListingId))
                advanceUntilIdle()

                // Then
                skipItems(1) // Skip Loading
                val successState = awaitItem()
                assertTrue(successState is ListingDetailsState.Success)
                assertEquals(customListingId, (successState as ListingDetailsState.Success).detail.id)

                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) { getListingDetailsUseCase(customListingId) }
        }

    @Test
    fun `handleIntent LoadDetails emits Loading then Error on NetworkUnavailable`() =
        runTest {
            // Given
            val networkError = DomainError.NetworkUnavailable("No internet connection")
            val errorResult = DataResult.Failure(networkError)
            coEvery { getListingDetailsUseCase(testListingId) } returns errorResult

            // When
            classUnderTest.state.test {
                // Skip initial state
                assertEquals(ListingDetailsState.NotInitialized, awaitItem())

                classUnderTest.handleIntent(ListingDetailsIntent.LoadDetails(testListingId))
                advanceUntilIdle()

                // Then
                assertEquals(ListingDetailsState.Loading, awaitItem())
                val errorState = awaitItem()
                assertTrue(errorState is ListingDetailsState.Error)
                assertEquals(networkError, (errorState as ListingDetailsState.Error).error)

                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) { getListingDetailsUseCase(testListingId) }
        }

    @Test
    fun `handleIntent LoadDetails emits Loading then Error on ServerError`() =
        runTest {
            // Given
            val serverError = DomainError.ServerError("Internal server error")
            val errorResult = DataResult.Failure(serverError)
            coEvery { getListingDetailsUseCase(testListingId) } returns errorResult

            // When
            classUnderTest.state.test {
                // Skip initial state
                assertEquals(ListingDetailsState.NotInitialized, awaitItem())

                classUnderTest.handleIntent(ListingDetailsIntent.LoadDetails(testListingId))
                advanceUntilIdle()

                // Then
                assertEquals(ListingDetailsState.Loading, awaitItem())
                val errorState = awaitItem()
                assertTrue(errorState is ListingDetailsState.Error)
                assertEquals(serverError, (errorState as ListingDetailsState.Error).error)

                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) { getListingDetailsUseCase(testListingId) }
        }

    @Test
    fun `handleIntent LoadDetails emits Loading then Error on UnknownError`() =
        runTest {
            // Given
            val unknownError = DomainError.UnknownError("Something went wrong")
            val errorResult = DataResult.Failure(unknownError)
            coEvery { getListingDetailsUseCase(testListingId) } returns errorResult

            // When
            classUnderTest.state.test {
                // Skip initial state
                assertEquals(ListingDetailsState.NotInitialized, awaitItem())

                classUnderTest.handleIntent(ListingDetailsIntent.LoadDetails(testListingId))
                advanceUntilIdle()

                // Then
                assertEquals(ListingDetailsState.Loading, awaitItem())
                val errorState = awaitItem()
                assertTrue(errorState is ListingDetailsState.Error)
                assertEquals(unknownError, (errorState as ListingDetailsState.Error).error)

                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) { getListingDetailsUseCase(testListingId) }
        }

    // ========== Retry Intent Tests ==========

    @Test
    fun `handleIntent Retry successfully loads details after previous failure`() =
        runTest {
            // Given
            val errorResult = DataResult.Failure(DomainError.ServerError())
            val successResult = DataResult.Success(sampleListingDetail)
            coEvery { getListingDetailsUseCase(testListingId) } returnsMany
                listOf(
                    errorResult,
                    successResult,
                )

            // When
            classUnderTest.state.test {
                // Skip initial state
                assertEquals(ListingDetailsState.NotInitialized, awaitItem())

                // First attempt
                classUnderTest.handleIntent(ListingDetailsIntent.LoadDetails(testListingId))
                advanceUntilIdle()
                skipItems(1) // Skip Loading
                assertTrue(awaitItem() is ListingDetailsState.Error)

                // Retry
                classUnderTest.handleIntent(ListingDetailsIntent.Retry)
                advanceUntilIdle()

                // Then
                skipItems(1) // Skip Loading
                val successState = awaitItem()
                assertTrue(successState is ListingDetailsState.Success)
                coVerify(exactly = 2) { getListingDetailsUseCase(testListingId) }

                cancelAndIgnoreRemainingEvents()
            }
        }

    // ========== NavigateBack Intent Tests ==========

    @Test
    fun `handleIntent NavigateBack emits NavigateBack effect`() =
        runTest {
            // When
            classUnderTest.effect.test {
                classUnderTest.handleIntent(ListingDetailsIntent.NavigateBack)
                advanceUntilIdle()

                // Then
                val effect = awaitItem()
                assertTrue(effect is ListingDetailsEffect.NavigateBack)

                cancelAndIgnoreRemainingEvents()
            }
        }
}
