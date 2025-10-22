package com.tneagu.realestateapp.features.listingdetails.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tneagu.realestateapp.core.domain.model.DataResult
import com.tneagu.realestateapp.features.listingdetails.domain.usecase.GetListingDetailsUseCase
import com.tneagu.realestateapp.features.listingdetails.presentation.mvi.ListingDetailsEffect
import com.tneagu.realestateapp.features.listingdetails.presentation.mvi.ListingDetailsIntent
import com.tneagu.realestateapp.features.listingdetails.presentation.mvi.ListingDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for the listing details screen implementing MVI pattern.
 */
@HiltViewModel
class ListingDetailsViewModel
    @Inject
    constructor(
        private val getListingDetailsUseCase: GetListingDetailsUseCase,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val listingId: Int =
            checkNotNull(savedStateHandle["listingId"]) {
                "ListingDetailsViewModel requires a listingId argument"
            }

        private val _state = MutableStateFlow<ListingDetailsState>(ListingDetailsState.NotInitialized)
        val state: StateFlow<ListingDetailsState> = _state.asStateFlow()

        private val _effect = Channel<ListingDetailsEffect>(Channel.BUFFERED)
        val effect = _effect.receiveAsFlow()

        /**
         * Handles user intents and triggers appropriate state changes.
         *
         * @param intent The user intent to handle.
         */
        fun handleIntent(intent: ListingDetailsIntent) {
            when (intent) {
                is ListingDetailsIntent.LoadDetails -> loadDetails(intent.listingId)
                ListingDetailsIntent.Retry -> loadDetails(listingId)
                ListingDetailsIntent.NavigateBack -> navigateBack()
            }
        }

        private fun loadDetails(id: Int) {
            viewModelScope.launch {
                _state.value = ListingDetailsState.Loading

                val result =
                    withContext(Dispatchers.IO) {
                        getListingDetailsUseCase(id)
                    }

                when (result) {
                    is DataResult.Success -> {
                        _state.value = ListingDetailsState.Success(result.data)
                    }
                    is DataResult.Failure -> {
                        _state.value = ListingDetailsState.Error(result.error)
                    }
                }
            }
        }

        private fun navigateBack() {
            viewModelScope.launch {
                _effect.send(ListingDetailsEffect.NavigateBack)
            }
        }
    }
