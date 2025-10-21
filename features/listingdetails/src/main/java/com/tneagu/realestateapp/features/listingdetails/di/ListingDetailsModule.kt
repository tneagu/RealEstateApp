package com.tneagu.realestateapp.features.listingdetails.di

import com.tneagu.realestateapp.features.listingdetails.data.api.ListingDetailsApiService
import com.tneagu.realestateapp.features.listingdetails.data.repository.ListingDetailsRepositoryImpl
import com.tneagu.realestateapp.features.listingdetails.domain.repository.ListingDetailsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Hilt module that provides dependencies for the listing details feature.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ListingDetailsModule {

    @Binds
    @Singleton
    internal abstract fun bindListingDetailsRepository(
        impl: ListingDetailsRepositoryImpl
    ): ListingDetailsRepository

    companion object {
        @Provides
        internal fun provideListingDetailsApiService(retrofit: Retrofit): ListingDetailsApiService {
            return retrofit.create(ListingDetailsApiService::class.java)
        }
    }
}
