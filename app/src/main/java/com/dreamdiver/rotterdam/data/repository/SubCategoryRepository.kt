package com.dreamdiver.rotterdam.data.repository

import com.dreamdiver.rotterdam.data.api.RetrofitInstance
import com.dreamdiver.rotterdam.data.model.Service
import com.dreamdiver.rotterdam.data.model.SubCategory

class SubCategoryRepository {
    private val apiService = RetrofitInstance.api

    suspend fun getCategoryTree(categoryId: Int): Result<List<SubCategory>> {
        return try {
            val response = apiService.getCategoryTree(categoryId)
            if (response.success) {
                Result.success(response.data.subcategories)
            } else {
                Result.failure(Exception("Failed to load category tree"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSubCategories(categoryId: Int): Result<List<SubCategory>> {
        return try {
            val response = apiService.getSubCategories(categoryId)
            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception("Failed to load subcategories"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getServicesBySubCategory(subcategoryId: Int): Result<Pair<String, List<Service>>> {
        return try {
            val response = apiService.getServicesBySubCategory(subcategoryId)
            if (response.success) {
                Result.success(Pair(response.subcategory.name, response.data))
            } else {
                Result.failure(Exception("Failed to load services"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

