package com.dreamdiver.rotterdam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamdiver.rotterdam.data.model.Service
import com.dreamdiver.rotterdam.data.model.ServiceDetail
import com.dreamdiver.rotterdam.data.repository.ServiceRepository
import com.dreamdiver.rotterdam.data.repository.SubCategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ServiceViewModel(
    private val repository: ServiceRepository = ServiceRepository(),
    private val subCategoryRepository: SubCategoryRepository = SubCategoryRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ServiceUiState>(ServiceUiState.Loading)
    val uiState: StateFlow<ServiceUiState> = _uiState.asStateFlow()

    private val _detailState = MutableStateFlow<ServiceDetailState>(ServiceDetailState.Initial)
    val detailState: StateFlow<ServiceDetailState> = _detailState.asStateFlow()

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

    fun loadServicesBySubCategory(subcategoryId: Int) {
        viewModelScope.launch {
            _uiState.value = ServiceUiState.Loading
            subCategoryRepository.getServicesBySubCategory(subcategoryId)
                .onSuccess { (subcategoryName, services) ->
                    _uiState.value = ServiceUiState.Success(services, subcategoryName)
                }
                .onFailure { error ->
                    _uiState.value = ServiceUiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun loadServiceDetail(serviceId: Int) {
        viewModelScope.launch {
            _detailState.value = ServiceDetailState.Loading
            repository.getServiceById(serviceId)
                .onSuccess { serviceDetail ->
                    _detailState.value = ServiceDetailState.Success(serviceDetail)
                }
                .onFailure { error ->
                    _detailState.value = ServiceDetailState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun clearServiceDetail() {
        _detailState.value = ServiceDetailState.Initial
    }
}

sealed class ServiceUiState {
    object Loading : ServiceUiState()
    data class Success(val services: List<Service>, val categoryName: String) : ServiceUiState()
    data class Error(val message: String) : ServiceUiState()
}

sealed class ServiceDetailState {
    object Initial : ServiceDetailState()
    object Loading : ServiceDetailState()
    data class Success(val serviceDetail: ServiceDetail) : ServiceDetailState()
    data class Error(val message: String) : ServiceDetailState()
}

