package com.dreamdiver.rotterdam.data.api

import com.dreamdiver.rotterdam.data.model.CategoryResponse
import com.dreamdiver.rotterdam.data.model.SliderResponse
import retrofit2.http.GET

interface ApiService {
    @GET("api/v1/categories")
    suspend fun getCategories(): CategoryResponse

    @GET("api/v1/sliders")
    suspend fun getSliders(): SliderResponse
}

