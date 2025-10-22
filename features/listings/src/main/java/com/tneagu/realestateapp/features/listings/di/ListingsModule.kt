package com.tneagu.realestateapp.features.listings.di

import com.tneagu.realestateapp.features.listings.data.api.ListingsApiService
import com.tneagu.realestateapp.features.listings.data.repository.ListingsRepositoryImpl
import com.tneagu.realestateapp.features.listings.domain.repository.ListingsRepository
import dagger.Binds
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
abstract class ListingsModule {
    @Binds
    internal abstract fun bindListingsRepository(impl: ListingsRepositoryImpl): ListingsRepository

    companion object {
        @Provides
        internal fun provideListingsApiService(retrofit: Retrofit): ListingsApiService {
            return retrofit.create(ListingsApiService::class.java)
        }
    }
}
