package com.tneagu.realestateapp.features.listingdetails.data.converter

import com.tneagu.realestateapp.core.domain.model.OfferType
import com.tneagu.realestateapp.features.listingdetails.data.dto.ListingDetailDto
import com.tneagu.realestateapp.features.listingdetails.domain.model.ListingDetail
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ListingDetailConverterTest {

    private lateinit var classUnderTest: ListingDetailConverter

    @BeforeEach
    fun setup() {
        classUnderTest = ListingDetailConverter()
    }

    @ParameterizedTest
    @CsvSource(
        "1, RENT",
        "2, SALE",
        "3, UNKNOWN"
    )
    fun `convert dto with all fields populated`(
        offerTypeInt: Int,
        expectedOfferType: OfferType
    ) {
        // Given
        val dto = ListingDetailDto(
            id = 1,
            bedrooms = 3,
            city = "Paris",
            area = 120.5,
            url = "https://example.com/image.jpg",
            price = 450000.0,
            professional = "Real Estate Pro",
            propertyType = "Apartment",
            offerType = offerTypeInt,
            rooms = 5
        )

        // When
        val result = classUnderTest.convert(dto)

        // Then
        val expected = ListingDetail(
            id = 1,
            bedrooms = 3,
            city = "Paris",
            area = 120.5,
            imageUrl = "https://example.com/image.jpg",
            price = 450000.0,
            professional = "Real Estate Pro",
            propertyType = "Apartment",
            offerType = expectedOfferType,
            rooms = 5
        )
        assertEquals(expected, result)
    }

    @Test
    fun `convert dto with all nullable fields set to null`() {
        // Given
        val dto = ListingDetailDto(
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
        val expected = ListingDetail(
            id = 2,
            bedrooms = null,
            city = "Lyon",
            area = 80.0,
            imageUrl = null,
            price = 250000.0,
            professional = "Another Pro",
            propertyType = "House",
            offerType = OfferType.SALE,
            rooms = null
        )
        assertEquals(expected, result)
    }
}
