package com.tneagu.realestateapp.features.listings.data.repository

import com.tneagu.realestateapp.core.domain.model.DataResult
import com.tneagu.realestateapp.core.domain.model.DomainError
import com.tneagu.realestateapp.features.listings.data.api.ListingsApiService
import com.tneagu.realestateapp.features.listings.data.converter.ListingsResponseConverter
import com.tneagu.realestateapp.features.listings.domain.model.Listing
import com.tneagu.realestateapp.features.listings.domain.repository.ListingsRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of ListingsRepository.
 * Handles API calls and maps infrastructure errors to domain errors.
 */
class ListingsRepositoryImpl @Inject constructor(
    private val apiService: ListingsApiService,
    private val converter: ListingsResponseConverter
) : ListingsRepository {

    override suspend fun getListings(): DataResult<List<Listing>> {
        return try {
            val response = apiService.getListings()
            DataResult.Success(converter.convert(response))
        } catch (e: HttpException) {
            DataResult.Failure(
                DomainError.ServerError(
                    message = "HTTP ${e.code()}: ${e.message()}"
                )
            )
        } catch (e: IOException) {
            DataResult.Failure(
                DomainError.NetworkUnavailable(
                    message = e.message
                )
            )
        } catch (e: Exception) {
            DataResult.Failure(
                DomainError.UnknownError(
                    message = "${e::class.simpleName}: ${e.message}"
                )
            )
        }
    }
}
