package com.tneagu.realestateapp.features.listings.data.converter

import com.tneagu.realestateapp.core.domain.model.OfferType
import com.tneagu.realestateapp.features.listings.data.dto.ListingDto
import com.tneagu.realestateapp.features.listings.data.dto.ListingsResponseDto
import com.tneagu.realestateapp.features.listings.domain.model.Listing
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ListingsResponseConverterTest {

    private val listingConverter = mockk<ListingConverter>()
    private lateinit var classUnderTest: ListingsResponseConverter

    @BeforeEach
    fun setup() {
        classUnderTest = ListingsResponseConverter(listingConverter)
    }

    @Test
    fun `convert response with single listing returns result from listing converter`() {
        // Given
        val listingDto = ListingDto(
            id = 1,
            bedrooms = 2,
            city = "Paris",
            area = 75.0,
            url = "https://example.com/image.jpg",
            price = 300000.0,
            professional = "Pro Agent",
            propertyType = "Flat",
            offerType = 1,
            rooms = 3
        )
        val responseDto = ListingsResponseDto(
            items = listOf(listingDto),
            totalCount = 1
        )

        val expectedListing = Listing(
            id = 1,
            bedrooms = 2,
            city = "Paris",
            area = 75.0,
            imageUrl = "https://example.com/image.jpg",
            price = 300000.0,
            professional = "Pro Agent",
            propertyType = "Flat",
            offerType = OfferType.RENT,
            rooms = 3
        )

        every { listingConverter.convert(listingDto) } returns expectedListing

        // When
        val result = classUnderTest.convert(responseDto)

        // Then
        assertEquals(listOf(expectedListing), result)
        verify(exactly = 1) { listingConverter.convert(listingDto) }
    }
}
