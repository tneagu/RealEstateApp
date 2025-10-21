package com.tneagu.realestateapp.features.listings.di

import com.tneagu.realestateapp.features.listings.data.api.ListingsApiService
import com.tneagu.realestateapp.features.listings.data.converter.ListingConverter
import com.tneagu.realestateapp.features.listings.data.converter.ListingsResponseConverter
import com.tneagu.realestateapp.features.listings.data.repository.ListingsRepositoryImpl
import com.tneagu.realestateapp.features.listings.domain.repository.ListingsRepository
import com.tneagu.realestateapp.features.listings.domain.usecase.GetListingsUseCase
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
    internal fun provideListingsApiService(retrofit: Retrofit): ListingsApiService {
        return retrofit.create(ListingsApiService::class.java)
    }

    @Provides
    internal fun provideListingConverter(): ListingConverter {
        return ListingConverter()
    }

    @Provides
    internal fun provideListingsResponseConverter(
        listingConverter: ListingConverter
    ): ListingsResponseConverter {
        return ListingsResponseConverter(listingConverter)
    }

    @Provides
    internal fun provideListingsRepository(
        apiService: ListingsApiService,
        converter: ListingsResponseConverter
    ): ListingsRepository {
        return ListingsRepositoryImpl(apiService, converter)
    }

    @Provides
    fun provideGetListingsUseCase(
        repository: ListingsRepository
    ): GetListingsUseCase {
        return GetListingsUseCase(repository)
    }
}
