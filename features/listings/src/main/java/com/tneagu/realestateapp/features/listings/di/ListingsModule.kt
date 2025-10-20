package com.tneagu.realestateapp.features.listings.di

import com.tneagu.realestateapp.features.listings.data.api.ListingsApiService
import com.tneagu.realestateapp.features.listings.data.converter.ListingConverter
import com.tneagu.realestateapp.features.listings.data.converter.ListingsResponseConverter
import com.tneagu.realestateapp.features.listings.data.repository.ListingsRepositoryImpl
import com.tneagu.realestateapp.features.listings.domain.repository.ListingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

/**
 * Hilt module providing dependencies for the listings feature.
 */
@Module
@InstallIn(SingletonComponent::class)
object ListingsModule {

    @Provides
    fun provideListingsApiService(retrofit: Retrofit): ListingsApiService {
        return retrofit.create(ListingsApiService::class.java)
    }

    @Provides
    fun provideListingConverter(): ListingConverter {
        return ListingConverter()
    }

    @Provides
    fun provideListingsResponseConverter(
        listingConverter: ListingConverter
    ): ListingsResponseConverter {
        return ListingsResponseConverter(listingConverter)
    }

    @Provides
    fun provideListingsRepository(
        apiService: ListingsApiService,
        converter: ListingsResponseConverter
    ): ListingsRepository {
        return ListingsRepositoryImpl(apiService, converter)
    }
}
