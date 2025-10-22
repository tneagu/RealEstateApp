package com.tneagu.realestateapp.features.listingdetails.data.repository

import com.tneagu.realestateapp.core.domain.model.DataResult
import com.tneagu.realestateapp.core.domain.model.DomainError
import com.tneagu.realestateapp.core.domain.model.OfferType
import com.tneagu.realestateapp.features.listingdetails.data.api.ListingDetailsApiService
import com.tneagu.realestateapp.features.listingdetails.data.converter.ListingDetailConverter
import com.tneagu.realestateapp.features.listingdetails.data.dto.ListingDetailDto
import com.tneagu.realestateapp.features.listingdetails.domain.model.ListingDetail
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ListingDetailsRepositoryImplTest {
    private val apiService = mockk<ListingDetailsApiService>()
    private val converter = mockk<ListingDetailConverter>()
    private lateinit var classUnderTest: ListingDetailsRepositoryImpl

    @BeforeEach
    fun setup() {
        classUnderTest = ListingDetailsRepositoryImpl(apiService, converter)
    }

    @Test
    fun `getListingDetails returns Success with converted data when API call succeeds`() =
        runTest {
            // Given
            val listingId = 1
            val responseDto = mockk<ListingDetailDto>()
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

            coEvery { apiService.getListingDetails(listingId) } returns responseDto
            coEvery { converter.convert(responseDto) } returns expectedDetail

            // When
            val result = classUnderTest.getListingDetails(listingId)

            // Then
            assertTrue(result is DataResult.Success)
            assertEquals(expectedDetail, (result as DataResult.Success).data)
        }

    @Test
    fun `getListingDetails returns ServerError with HTTP code in message when HttpException occurs`() =
        runTest {
            // Given
            val listingId = 1
            val httpException =
                HttpException(
                    Response.error<Any>(404, mockk(relaxed = true)),
                )
            coEvery { apiService.getListingDetails(listingId) } throws httpException

            // When
            val result = classUnderTest.getListingDetails(listingId)

            // Then
            assertTrue(result is DataResult.Failure)
            val error = (result as DataResult.Failure).error
            assertTrue(error is DomainError.ServerError)
            assertTrue(error.message?.contains("HTTP 404") == true)
        }

    @Test
    fun `getListingDetails returns NetworkUnavailable with exception message when IOException occurs`() =
        runTest {
            // Given
            val listingId = 1
            val ioException = IOException("Network timeout")
            coEvery { apiService.getListingDetails(listingId) } throws ioException

            // When
            val result = classUnderTest.getListingDetails(listingId)

            // Then
            assertTrue(result is DataResult.Failure)
            val error = (result as DataResult.Failure).error
            assertTrue(error is DomainError.NetworkUnavailable)
            assertEquals("Network timeout", error.message)
        }

    @Test
    fun `getListingDetails returns UnknownError with exception type and message when generic exception occurs`() =
        runTest {
            // Given
            val listingId = 1
            val genericException = IllegalStateException("Something went wrong")
            coEvery { apiService.getListingDetails(listingId) } throws genericException

            // When
            val result = classUnderTest.getListingDetails(listingId)

            // Then
            assertTrue(result is DataResult.Failure)
            val error = (result as DataResult.Failure).error
            assertTrue(error is DomainError.UnknownError)
            assertTrue(error.message?.contains("IllegalStateException") == true)
            assertTrue(error.message?.contains("Something went wrong") == true)
        }
}
