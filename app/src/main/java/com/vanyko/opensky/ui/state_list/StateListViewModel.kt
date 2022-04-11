package com.vanyko.opensky.ui.state_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanyko.opensky.data.OpenSkyRepository
import com.vanyko.opensky.data.model.OpenSkyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ListUiState {

    val isLoading: Boolean

    data class NoData(
        override val isLoading: Boolean,
    ) : ListUiState

    data class HasData(
        val skyStates: List<OpenSkyState>,
        override val isLoading: Boolean,
    ) : ListUiState
}

private data class ListViewModelState(
    val skyStates: List<OpenSkyState>? = null,
    val isLoading: Boolean = false,
) {
    fun toUiState(): ListUiState =
        if (skyStates == null) {
            ListUiState.NoData(
                isLoading = isLoading,
            )
        } else {
            ListUiState.HasData(
                skyStates = skyStates,
                isLoading = isLoading,
            )
        }
}

@HiltViewModel
class StateListViewModel @Inject constructor(
    private val openSkyRepository: OpenSkyRepository
): ViewModel() {
    private val TAG = "StateListViewModel"
    private val viewModelState = MutableStateFlow(ListViewModelState(isLoading = true))

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        refreshSkyStates()
    }

    fun refreshSkyStates() {
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = openSkyRepository.fetchSkyStates()
            Log.i(TAG, "fetchSkyStates response = $result")

            viewModelState.update {
                when (result) {
                    is com.vanyko.opensky.data.Result.Success -> it.copy(skyStates = result.data, isLoading = false)
                    is com.vanyko.opensky.data.Result.Error -> it.copy(isLoading = false)
                }
            }
        }
    }
}