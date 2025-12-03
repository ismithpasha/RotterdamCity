package com.dreamdiver.rotterdam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamdiver.rotterdam.data.model.Category
import com.dreamdiver.rotterdam.data.model.Service
import com.dreamdiver.rotterdam.data.model.TrendingItem
import com.dreamdiver.rotterdam.data.repository.CategoryRepository
import com.dreamdiver.rotterdam.data.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val categoryRepository: CategoryRepository = CategoryRepository(),
    private val homeRepository: HomeRepository = HomeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            try {
                // Load all data in parallel
                val categoriesResult = categoryRepository.getCategories()
                val featuredResult = homeRepository.getFeaturedServices()
                val trendingResult = homeRepository.getTrending()

                if (categoriesResult.isSuccess && featuredResult.isSuccess && trendingResult.isSuccess) {
                    _uiState.value = HomeUiState.Success(
                        categories = categoriesResult.getOrNull() ?: emptyList(),
                        featuredServices = featuredResult.getOrNull() ?: emptyList(),
                        trendingItems = trendingResult.getOrNull() ?: emptyList()
                    )
                } else {
                    val error = categoriesResult.exceptionOrNull()
                        ?: featuredResult.exceptionOrNull()
                        ?: trendingResult.exceptionOrNull()
                    _uiState.value = HomeUiState.Error(error?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val categories: List<Category>,
        val featuredServices: List<Service>,
        val trendingItems: List<TrendingItem>
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

