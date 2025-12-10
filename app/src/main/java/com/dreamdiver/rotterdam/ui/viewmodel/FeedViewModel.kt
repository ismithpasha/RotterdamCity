package com.dreamdiver.rotterdam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamdiver.rotterdam.data.api.RetrofitInstance
import com.dreamdiver.rotterdam.data.model.Feed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class FeedState {
    object Idle : FeedState()
    object Loading : FeedState()
    data class Success(val feeds: List<Feed>) : FeedState()
    data class Error(val message: String) : FeedState()
}

class FeedViewModel : ViewModel() {
    private val _feedState = MutableStateFlow<FeedState>(FeedState.Idle)
    val feedState: StateFlow<FeedState> = _feedState.asStateFlow()

    private val apiService = RetrofitInstance.api

    fun loadFeedsByType(type: String) {
        viewModelScope.launch {
            _feedState.value = FeedState.Loading
            try {
                val response = apiService.getFeedsByType(type)
                if (response.success) {
                    _feedState.value = FeedState.Success(response.data)
                } else {
                    _feedState.value = FeedState.Error(response.message)
                }
            } catch (e: Exception) {
                _feedState.value = FeedState.Error(e.message ?: "Failed to load feeds")
            }
        }
    }

    fun loadAllFeeds() {
        viewModelScope.launch {
            _feedState.value = FeedState.Loading
            try {
                val response = apiService.getAllFeeds()
                if (response.success) {
                    _feedState.value = FeedState.Success(response.data)
                } else {
                    _feedState.value = FeedState.Error(response.message)
                }
            } catch (e: Exception) {
                _feedState.value = FeedState.Error(e.message ?: "Failed to load feeds")
            }
        }
    }

    fun loadLatestFeeds() {
        viewModelScope.launch {
            _feedState.value = FeedState.Loading
            try {
                val response = apiService.getLatestFeeds()
                if (response.success) {
                    _feedState.value = FeedState.Success(response.data)
                } else {
                    _feedState.value = FeedState.Error(response.message)
                }
            } catch (e: Exception) {
                _feedState.value = FeedState.Error(e.message ?: "Failed to load feeds")
            }
        }
    }
}

