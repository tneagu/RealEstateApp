package com.tneagu.realestateapp.features.listingdetails.domain.usecase

import com.tneagu.realestateapp.core.domain.model.DataResult
import com.tneagu.realestateapp.core.domain.model.DomainError
import com.tneagu.realestateapp.core.domain.model.OfferType
import com.tneagu.realestateapp.features.listingdetails.domain.model.ListingDetail
import com.tneagu.realestateapp.features.listingdetails.domain.repository.ListingDetailsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetListingDetailsUseCaseTest {
    private val repository = mockk<ListingDetailsRepository>()
    private lateinit var classUnderTest: GetListingDetailsUseCase

    @BeforeEach
    fun setup() {
        classUnderTest = GetListingDetailsUseCase(repository)
    }

    @Test
    fun `invoke returns Success when repository returns Success`() =
        runTest {
            // Given
            val listingId = 1
            val expectedDetail =
                ListingDetail(
                    id = 1,
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
            val expectedResult = DataResult.Success(expectedDetail)
            coEvery { repository.getListingDetails(listingId) } returns expectedResult

            // When
            val result = classUnderTest(listingId)

            // Then
            assertTrue(result is DataResult.Success)
            assertEquals(expectedDetail, (result as DataResult.Success).data)
            coVerify(exactly = 1) { repository.getListingDetails(listingId) }
        }

    @Test
    fun `invoke returns ServerError when repository returns ServerError`() =
        runTest {
            // Given
            val listingId = 1
            val expectedError = DomainError.ServerError(message = "HTTP 500: Internal Server Error")
            val expectedResult = DataResult.Failure(expectedError)
            coEvery { repository.getListingDetails(listingId) } returns expectedResult

            // When
            val result = classUnderTest(listingId)

            // Then
            assertTrue(result is DataResult.Failure)
            val error = (result as DataResult.Failure).error
            assertTrue(error is DomainError.ServerError)
            assertEquals("HTTP 500: Internal Server Error", error.message)
            coVerify(exactly = 1) { repository.getListingDetails(listingId) }
        }

    @Test
    fun `invoke returns NetworkUnavailable when repository returns NetworkUnavailable`() =
        runTest {
            // Given
            val listingId = 1
            val expectedError = DomainError.NetworkUnavailable(message = "No internet connection")
            val expectedResult = DataResult.Failure(expectedError)
            coEvery { repository.getListingDetails(listingId) } returns expectedResult

            // When
            val result = classUnderTest(listingId)

            // Then
            assertTrue(result is DataResult.Failure)
            val error = (result as DataResult.Failure).error
            assertTrue(error is DomainError.NetworkUnavailable)
            assertEquals("No internet connection", error.message)
            coVerify(exactly = 1) { repository.getListingDetails(listingId) }
        }

    @Test
    fun `invoke returns UnknownError when repository returns UnknownError`() =
        runTest {
            // Given
            val listingId = 1
            val expectedError = DomainError.UnknownError(message = "Unexpected error occurred")
            val expectedResult = DataResult.Failure(expectedError)
            coEvery { repository.getListingDetails(listingId) } returns expectedResult

            // When
            val result = classUnderTest(listingId)

            // Then
            assertTrue(result is DataResult.Failure)
            val error = (result as DataResult.Failure).error
            assertTrue(error is DomainError.UnknownError)
            assertEquals("Unexpected error occurred", error.message)
            coVerify(exactly = 1) { repository.getListingDetails(listingId) }
        }
}
