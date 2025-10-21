package com.tneagu.realestateapp.features.listingdetails.di

import com.tneagu.realestateapp.features.listingdetails.data.api.ListingDetailsApiService
import com.tneagu.realestateapp.features.listingdetails.data.converter.ListingDetailConverter
import com.tneagu.realestateapp.features.listingdetails.data.repository.ListingDetailsRepositoryImpl
import com.tneagu.realestateapp.features.listingdetails.domain.repository.ListingDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

/**
 * Hilt module that provides dependencies for the listing details feature.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object ListingDetailsModule {

    /**
     * Provides the [ListingDetailsApiService] instance.
     */
    @Provides
    fun provideListingDetailsApiService(retrofit: Retrofit): ListingDetailsApiService {
        return retrofit.create(ListingDetailsApiService::class.java)
    }

    /**
     * Provides the [ListingDetailConverter] instance.
     */
    @Provides
    fun provideListingDetailConverter(): ListingDetailConverter {
        return ListingDetailConverter()
    }

    /**
     * Provides the [ListingDetailsRepository] implementation.
     */
    @Provides
    fun provideListingDetailsRepository(
        apiService: ListingDetailsApiService,
        converter: ListingDetailConverter
    ): ListingDetailsRepository {
        return ListingDetailsRepositoryImpl(apiService, converter)
    }
}
