package com.tneagu.realestateapp.features.listings.domain.usecase

import com.tneagu.realestateapp.core.domain.model.DataResult
import com.tneagu.realestateapp.features.listings.domain.model.Listing
import com.tneagu.realestateapp.features.listings.domain.repository.ListingsRepository
import javax.inject.Inject

/**
 * Use case for retrieving real estate listings.
 * Encapsulates the business logic for fetching listings from the repository.
 */
class GetListingsUseCase
    @Inject
    constructor(
        private val repository: ListingsRepository,
    ) {
        /**
         * Retrieves all listings.
         *
         * @return DataResult containing a list of Listing domain models or a domain error.
         */
        suspend operator fun invoke(): DataResult<List<Listing>> {
            return repository.getListings()
        }
    }
