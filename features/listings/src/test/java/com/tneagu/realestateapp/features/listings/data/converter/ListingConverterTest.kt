package com.tneagu.realestateapp.features.listings.data.converter

import com.tneagu.realestateapp.features.listings.data.dto.ListingDto
import com.tneagu.realestateapp.features.listings.domain.model.Listing
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ListingConverterTest {

    private lateinit var classUnderTest: ListingConverter

    @BeforeEach
    fun setup() {
        classUnderTest = ListingConverter()
    }

    @Test
    fun `convert dto with all fields populated`() {
        // Given
        val dto = ListingDto(
            id = 1,
            bedrooms = 3,
            city = "Paris",
            area = 120.5,
            url = "https://example.com/image.jpg",
            price = 450000.0,
            professional = "Real Estate Pro",
            propertyType = "Apartment",
            offerType = 1,
            rooms = 5
        )

        // When
        val result = classUnderTest.convert(dto)

        // Then
        val expected = Listing(
            id = 1,
            bedrooms = 3,
            city = "Paris",
            area = 120.5,
            imageUrl = "https://example.com/image.jpg",
            price = 450000.0,
            professional = "Real Estate Pro",
            propertyType = "Apartment",
            rooms = 5
        )
        assertEquals(expected, result)
    }

    @Test
    fun `convert dto with all nullable fields set to null`() {
        // Given
        val dto = ListingDto(
            id = 2,
            bedrooms = null,
            city = "Lyon",
            area = 80.0,
            url = null,
            price = 250000.0,
            professional = "Another Pro",
            propertyType = "House",
            offerType = 2,
            rooms = null
        )

        // When
        val result = classUnderTest.convert(dto)

        // Then
        val expected = Listing(
            id = 2,
            bedrooms = null,
            city = "Lyon",
            area = 80.0,
            imageUrl = null,
            price = 250000.0,
            professional = "Another Pro",
            propertyType = "House",
            rooms = null
        )
        assertEquals(expected, result)
    }
}
