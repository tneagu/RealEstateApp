package com.tneagu.realestateapp.features.listings.data.repository

import com.tneagu.realestateapp.core.domain.model.DataResult
import com.tneagu.realestateapp.core.domain.model.DomainError
import com.tneagu.realestateapp.features.listings.data.api.ListingsApiService
import com.tneagu.realestateapp.features.listings.data.converter.ListingsResponseConverter
import com.tneagu.realestateapp.features.listings.data.dto.ListingsResponseDto
import com.tneagu.realestateapp.features.listings.domain.model.Listing
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

class ListingsRepositoryImplTest {

    private val apiService = mockk<ListingsApiService>()
    private val converter = mockk<ListingsResponseConverter>()
    private lateinit var classUnderTest: ListingsRepositoryImpl

    @BeforeEach
    fun setup() {
        classUnderTest = ListingsRepositoryImpl(apiService, converter)
    }

    @Test
    fun `getListings returns Success with converted data when API call succeeds`() = runTest {
        // Given
        val responseDto = mockk<ListingsResponseDto>()
        val expectedListings = listOf(
            Listing(
                id = 1,
                bedrooms = 2,
                city = "Paris",
                area = 75.0,
                imageUrl = "https://example.com/image.jpg",
                price = 300000.0,
                professional = "Pro Agent",
                propertyType = "Flat",
                rooms = 3
            )
        )

        coEvery { apiService.getListings() } returns responseDto
        coEvery { converter.convert(responseDto) } returns expectedListings

        // When
        val result = classUnderTest.getListings()

        // Then
        assertTrue(result is DataResult.Success)
        assertEquals(expectedListings, (result as DataResult.Success).data)
    }

    @Test
    fun `getListings returns ServerError with HTTP code in message when HttpException occurs`() = runTest {
        // Given
        val httpException = HttpException(
            Response.error<Any>(404, mockk(relaxed = true))
        )
        coEvery { apiService.getListings() } throws httpException

        // When
        val result = classUnderTest.getListings()

        // Then
        assertTrue(result is DataResult.Failure)
        val error = (result as DataResult.Failure).error
        assertTrue(error is DomainError.ServerError)
        assertTrue(error.message?.contains("HTTP 404") == true)
    }

    @Test
    fun `getListings returns NetworkUnavailable with exception message when IOException occurs`() = runTest {
        // Given
        val ioException = IOException("Network timeout")
        coEvery { apiService.getListings() } throws ioException

        // When
        val result = classUnderTest.getListings()

        // Then
        assertTrue(result is DataResult.Failure)
        val error = (result as DataResult.Failure).error
        assertTrue(error is DomainError.NetworkUnavailable)
        assertEquals("Network timeout", error.message)
    }

    @Test
    fun `getListings returns UnknownError with exception type and message when generic exception occurs`() = runTest {
        // Given
        val genericException = IllegalStateException("Something went wrong")
        coEvery { apiService.getListings() } throws genericException

        // When
        val result = classUnderTest.getListings()

        // Then
        assertTrue(result is DataResult.Failure)
        val error = (result as DataResult.Failure).error
        assertTrue(error is DomainError.UnknownError)
        assertTrue(error.message?.contains("IllegalStateException") == true)
        assertTrue(error.message?.contains("Something went wrong") == true)
    }
}
