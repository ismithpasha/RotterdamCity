package com.dreamdiver.rotterdam.data.api

import com.dreamdiver.rotterdam.data.model.CategoryResponse
import com.dreamdiver.rotterdam.data.model.ServiceResponse
import com.dreamdiver.rotterdam.data.model.SliderResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/v1/categories")
    suspend fun getCategories(): CategoryResponse

    @GET("api/v1/sliders")
    suspend fun getSliders(): SliderResponse

    @GET("api/v1/services")
    suspend fun getServices(@Query("category_id") categoryId: Int): ServiceResponse
}

