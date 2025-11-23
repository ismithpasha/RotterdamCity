package com.dreamdiver.rotterdam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamdiver.rotterdam.data.model.Slider
import com.dreamdiver.rotterdam.data.repository.SliderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SliderViewModel(
    private val repository: SliderRepository = SliderRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<SliderUiState>(SliderUiState.Loading)
    val uiState: StateFlow<SliderUiState> = _uiState.asStateFlow()

    init {
        loadSliders()
    }

    fun loadSliders() {
        viewModelScope.launch {
            _uiState.value = SliderUiState.Loading
            repository.getSliders()
                .onSuccess { sliders ->
                    _uiState.value = SliderUiState.Success(sliders)
                }
                .onFailure { error ->
                    _uiState.value = SliderUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
}

sealed class SliderUiState {
    object Loading : SliderUiState()
    data class Success(val sliders: List<Slider>) : SliderUiState()
    data class Error(val message: String) : SliderUiState()
}

