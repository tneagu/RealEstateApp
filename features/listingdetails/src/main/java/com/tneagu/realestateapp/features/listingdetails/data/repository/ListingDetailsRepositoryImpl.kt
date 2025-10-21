package com.tneagu.realestateapp.features.listingdetails.data.repository

import com.tneagu.realestateapp.core.domain.model.DataResult
import com.tneagu.realestateapp.core.domain.model.DomainError
import com.tneagu.realestateapp.features.listingdetails.data.api.ListingDetailsApiService
import com.tneagu.realestateapp.features.listingdetails.data.converter.ListingDetailConverter
import com.tneagu.realestateapp.features.listingdetails.domain.model.ListingDetail
import com.tneagu.realestateapp.features.listingdetails.domain.repository.ListingDetailsRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of [ListingDetailsRepository] that fetches data from the remote API.
 */
internal class ListingDetailsRepositoryImpl @Inject constructor(
    private val apiService: ListingDetailsApiService,
    private val converter: ListingDetailConverter
) : ListingDetailsRepository {

    override suspend fun getListingDetails(listingId: Int): DataResult<ListingDetail> {
        return try {
            val response = apiService.getListingDetails(listingId)
            val listingDetail = converter.convert(response)
            DataResult.Success(listingDetail)
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
