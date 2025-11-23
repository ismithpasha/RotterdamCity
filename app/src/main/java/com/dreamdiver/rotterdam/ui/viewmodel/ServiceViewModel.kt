package com.dreamdiver.rotterdam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamdiver.rotterdam.data.model.Service
import com.dreamdiver.rotterdam.data.repository.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ServiceViewModel(
    private val repository: ServiceRepository = ServiceRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ServiceUiState>(ServiceUiState.Loading)
    val uiState: StateFlow<ServiceUiState> = _uiState.asStateFlow()

    fun loadServices(categoryId: Int, categoryName: String) {
        viewModelScope.launch {
            _uiState.value = ServiceUiState.Loading
            repository.getServices(categoryId)
                .onSuccess { services ->
                    _uiState.value = ServiceUiState.Success(services, categoryName)
                }
                .onFailure { error ->
                    _uiState.value = ServiceUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
}

sealed class ServiceUiState {
    object Loading : ServiceUiState()
    data class Success(val services: List<Service>, val categoryName: String) : ServiceUiState()
    data class Error(val message: String) : ServiceUiState()
}

