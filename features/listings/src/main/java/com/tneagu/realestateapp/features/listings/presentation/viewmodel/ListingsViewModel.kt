package com.tneagu.realestateapp.features.listings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tneagu.realestateapp.core.domain.model.DataResult
import com.tneagu.realestateapp.features.listings.domain.usecase.GetListingsUseCase
import com.tneagu.realestateapp.features.listings.presentation.mvi.ListingsEffect
import com.tneagu.realestateapp.features.listings.presentation.mvi.ListingsIntent
import com.tneagu.realestateapp.features.listings.presentation.mvi.ListingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the listings screen implementing MVI pattern.
 * Manages UI state, handles user intents, and emits one-time effects.
 */
@HiltViewModel
class ListingsViewModel @Inject constructor(
    private val getListingsUseCase: GetListingsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ListingsState>(ListingsState.NotInitialized)
    val state: StateFlow<ListingsState> = _state.asStateFlow()

    private val _effect = Channel<ListingsEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    /**
     * Handles user intents and triggers appropriate state changes.
     *
     * @param intent The user intent to handle.
     */
    fun handleIntent(intent: ListingsIntent) {
        when (intent) {
            ListingsIntent.LoadListings -> loadListings()
            ListingsIntent.Retry -> loadListings()
        }
    }

    /**
     * Loads listings from the use case and updates state accordingly.
     */
    private fun loadListings() {
        viewModelScope.launch {
            _state.value = ListingsState.Loading

            when (val result = getListingsUseCase()) {
                is DataResult.Success -> {
                    _state.value = ListingsState.Success(result.data)
                }
                is DataResult.Failure -> {
                    _state.value = ListingsState.Error(result.error)
                }
            }
        }
    }
}
