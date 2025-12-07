package com.dreamdiver.rotterdam.data.api

import com.dreamdiver.rotterdam.data.model.AuthResponse
import com.dreamdiver.rotterdam.data.model.CategoryResponse
import com.dreamdiver.rotterdam.data.model.CategoryTreeResponse
import com.dreamdiver.rotterdam.data.model.FeaturedServicesResponse
import com.dreamdiver.rotterdam.data.model.LoginRequest
import com.dreamdiver.rotterdam.data.model.ProfileUpdateResponse
import com.dreamdiver.rotterdam.data.model.RegisterRequest
import com.dreamdiver.rotterdam.data.model.ServiceDetailResponse
import com.dreamdiver.rotterdam.data.model.ServiceResponse
import com.dreamdiver.rotterdam.data.model.SliderResponse
import com.dreamdiver.rotterdam.data.model.SubCategoryResponse
import com.dreamdiver.rotterdam.data.model.SubCategoryServiceResponse
import com.dreamdiver.rotterdam.data.model.TrendingResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @GET("api/v1/categories")
    suspend fun getCategories(): CategoryResponse

    @GET("api/v1/sliders")
    suspend fun getSliders(): SliderResponse

    @GET("api/v1/services/featured")
    suspend fun getFeaturedServices(): FeaturedServicesResponse

    @GET("api/v1/trending")
    suspend fun getTrending(): TrendingResponse

    @GET("api/v1/subcategories/category/{categoryId}")
    suspend fun getSubCategories(@Path("categoryId") categoryId: Int): SubCategoryResponse

    @GET("api/v1/categories/{categoryId}/tree")
    suspend fun getCategoryTree(@Path("categoryId") categoryId: Int): CategoryTreeResponse

    @GET("api/v1/subcategories/{subcategoryId}/services")
    suspend fun getServicesBySubCategory(@Path("subcategoryId") subcategoryId: Int): SubCategoryServiceResponse

    @GET("api/v1/services")
    suspend fun getServices(@Query("category_id") categoryId: Int): ServiceResponse

    @GET("api/v1/services/{serviceId}")
    suspend fun getServiceById(@Path("serviceId") serviceId: Int): ServiceDetailResponse

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("api/v1/auth/profile")
    suspend fun getProfile(@Header("Authorization") token: String): ProfileUpdateResponse

    @Multipart
    @POST("api/v1/auth/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Part("_method") method: RequestBody,
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part("state") state: RequestBody?,
        @Part("zip_code") zipCode: RequestBody?,
        @Part("latitude") latitude: RequestBody?,
        @Part("longitude") longitude: RequestBody?,
        @Part("skill_category") skillCategory: RequestBody?,
        @Part("skills[]") skills: List<RequestBody>?,
        @Part("bio") bio: RequestBody?,
        @Part("hourly_rate") hourlyRate: RequestBody?,
        @Part("experience_years") experienceYears: RequestBody?,
        @Part("certifications[]") certifications: List<RequestBody>?,
        @Part("availability") availability: RequestBody?,
        @Part avatar: MultipartBody.Part?
    ): ProfileUpdateResponse
}
