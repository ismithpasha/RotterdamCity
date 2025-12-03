package com.dreamdiver.rotterdam.data.repository

import com.dreamdiver.rotterdam.data.api.RetrofitInstance
import com.dreamdiver.rotterdam.data.model.Service
import com.dreamdiver.rotterdam.data.model.TrendingItem

class HomeRepository {
    suspend fun getFeaturedServices(): Result<List<Service>> {
        return try {
            val response = RetrofitInstance.api.getFeaturedServices()
            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTrending(): Result<List<TrendingItem>> {
        return try {
            val response = RetrofitInstance.api.getTrending()
            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

