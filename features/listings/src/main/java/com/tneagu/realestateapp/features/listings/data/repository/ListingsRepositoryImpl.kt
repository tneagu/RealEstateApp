package com.tneagu.realestateapp.features.listings.data.repository

import com.tneagu.realestateapp.core.network.model.ApiResult
import com.tneagu.realestateapp.core.network.model.NetworkError
import com.tneagu.realestateapp.features.listings.data.api.ListingsApiService
import com.tneagu.realestateapp.features.listings.data.converter.ListingsResponseConverter
import com.tneagu.realestateapp.features.listings.domain.model.Listing
import com.tneagu.realestateapp.features.listings.domain.repository.ListingsRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of ListingsRepository.
 * Handles API calls and error handling for listings data.
 */
class ListingsRepositoryImpl @Inject constructor(
    private val apiService: ListingsApiService,
    private val converter: ListingsResponseConverter
) : ListingsRepository {

    override suspend fun getListings(): ApiResult<List<Listing>> {
        return try {
            val response = apiService.getListings()
            ApiResult.Success(converter.convert(response))
        } catch (e: HttpException) {
            ApiResult.Error(
                NetworkError.ServerError(
                    code = e.code(),
                    message = e.message()
                )
            )
        } catch (e: IOException) {
            ApiResult.Error(
                NetworkError.NetworkException(
                    message = "Network error occurred",
                    cause = e
                )
            )
        } catch (e: Exception) {
            ApiResult.Error(
                NetworkError.UnknownError(
                    message = "An unexpected error occurred",
                    cause = e
                )
            )
        }
    }
}
