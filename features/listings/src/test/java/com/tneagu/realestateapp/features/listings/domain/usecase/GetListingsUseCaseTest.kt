package com.tneagu.realestateapp.features.listings.domain.usecase

import com.tneagu.realestateapp.core.domain.model.DataResult
import com.tneagu.realestateapp.core.domain.model.DomainError
import com.tneagu.realestateapp.core.domain.model.OfferType
import com.tneagu.realestateapp.features.listings.domain.model.Listing
import com.tneagu.realestateapp.features.listings.domain.repository.ListingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetListingsUseCaseTest {
    private val repository = mockk<ListingsRepository>()
    private lateinit var classUnderTest: GetListingsUseCase

    @BeforeEach
    fun setup() {
        classUnderTest = GetListingsUseCase(repository)
    }

    @Test
    fun `invoke returns Success when repository returns Success`() =
        runTest {
            // Given
            val expectedListings =
                listOf(
                    Listing(
                        id = 1,
                        bedrooms = 2,
                        city = "Paris",
                        area = 75.0,
                        imageUrl = "https://example.com/image.jpg",
                        price = 300000.0,
                        professional = "Pro Agent",
                        propertyType = "Flat",
                        offerType = OfferType.RENT,
                        rooms = 3,
                    ),
                )
            val expectedResult = DataResult.Success(expectedListings)
            coEvery { repository.getListings() } returns expectedResult

            // When
            val result = classUnderTest()

            // Then
            assertTrue(result is DataResult.Success)
            assertEquals(expectedListings, (result as DataResult.Success).data)
            coVerify(exactly = 1) { repository.getListings() }
        }

    @Test
    fun `invoke returns ServerError when repository returns ServerError`() =
        runTest {
            // Given
            val expectedError = DomainError.ServerError(message = "HTTP 500: Internal Server Error")
            val expectedResult = DataResult.Failure(expectedError)
            coEvery { repository.getListings() } returns expectedResult

            // When
            val result = classUnderTest()

            // Then
            assertTrue(result is DataResult.Failure)
            val error = (result as DataResult.Failure).error
            assertTrue(error is DomainError.ServerError)
            assertEquals("HTTP 500: Internal Server Error", error.message)
            coVerify(exactly = 1) { repository.getListings() }
        }

    @Test
    fun `invoke returns NetworkUnavailable when repository returns NetworkUnavailable`() =
        runTest {
            // Given
            val expectedError = DomainError.NetworkUnavailable(message = "No internet connection")
            val expectedResult = DataResult.Failure(expectedError)
            coEvery { repository.getListings() } returns expectedResult

            // When
            val result = classUnderTest()

            // Then
            assertTrue(result is DataResult.Failure)
            val error = (result as DataResult.Failure).error
            assertTrue(error is DomainError.NetworkUnavailable)
            assertEquals("No internet connection", error.message)
            coVerify(exactly = 1) { repository.getListings() }
        }

    @Test
    fun `invoke returns UnknownError when repository returns UnknownError`() =
        runTest {
            // Given
            val expectedError = DomainError.UnknownError(message = "Unexpected error occurred")
            val expectedResult = DataResult.Failure(expectedError)
            coEvery { repository.getListings() } returns expectedResult

            // When
            val result = classUnderTest()

            // Then
            assertTrue(result is DataResult.Failure)
            val error = (result as DataResult.Failure).error
            assertTrue(error is DomainError.UnknownError)
            assertEquals("Unexpected error occurred", error.message)
            coVerify(exactly = 1) { repository.getListings() }
        }
}
