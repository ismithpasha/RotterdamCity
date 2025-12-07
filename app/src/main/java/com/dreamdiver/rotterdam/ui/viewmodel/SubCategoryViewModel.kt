package com.dreamdiver.rotterdam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamdiver.rotterdam.data.model.SubCategory
import com.dreamdiver.rotterdam.data.repository.SubCategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubCategoryViewModel(
    private val repository: SubCategoryRepository = SubCategoryRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<SubCategoryUiState>(SubCategoryUiState.Loading)
    val uiState: StateFlow<SubCategoryUiState> = _uiState.asStateFlow()

    fun loadSubCategories(categoryId: Int, categoryName: String) {
        viewModelScope.launch {
            _uiState.value = SubCategoryUiState.Loading
            repository.getCategoryTree(categoryId)
                .onSuccess { subCategories ->
                    _uiState.value = SubCategoryUiState.Success(categoryName, subCategories)
                }
                .onFailure { error ->
                    _uiState.value = SubCategoryUiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun loadNestedSubCategories(subCategory: SubCategory) {
        viewModelScope.launch {
            _uiState.value = SubCategoryUiState.Loading
            // Load the children of the subcategory
            _uiState.value = SubCategoryUiState.Success(subCategory.name, subCategory.children)
        }
    }
}

sealed class SubCategoryUiState {
    object Loading : SubCategoryUiState()
    data class Success(val categoryName: String, val subCategories: List<SubCategory>) : SubCategoryUiState()
    data class Error(val message: String) : SubCategoryUiState()
}

