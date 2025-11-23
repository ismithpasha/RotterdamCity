package com.dreamdiver.rotterdam.data.repository

import com.dreamdiver.rotterdam.data.api.RetrofitInstance
import com.dreamdiver.rotterdam.data.model.Service

class ServiceRepository {
    suspend fun getServices(categoryId: Int): Result<List<Service>> {
        return try {
            val response = RetrofitInstance.api.getServices(categoryId)
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

