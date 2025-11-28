package com.dreamdiver.rotterdam.data.model

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    val success: Boolean,
    val data: List<Category>,
    val message: String
)

data class Category(
    val id: Int,
    val name: String,
    val icon: String,
    val status: String,
    @SerializedName("services_count")
    val servicesCount: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class SliderResponse(
    val success: Boolean,
    val data: List<Slider>,
    val count: Int,
    val message: String
)

data class Slider(
    val id: Int,
    val title: String,
    @SerializedName("short_details")
    val shortDetails: String,
    val details: String,
    val image: String,
    val order: Int,
    val status: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class ServiceResponse(
    val success: Boolean,
    val data: List<Service>,
    val count: Int,
    val message: String
)

data class Service(
    val id: Int,
    @SerializedName("category_id")
    val categoryId: Int,
    val category: Category,
    val name: String,
    val phone: String,
    val address: String,
    val latitude: String?,
    val longitude: String?,
    @SerializedName("google_maps_url")
    val googleMapsUrl: String?,
    val description: String,
    @SerializedName("image_url")
    val image: String,
    val status: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class ServiceDetailResponse(
    val success: Boolean,
    val data: ServiceDetail,
    val message: String
)

data class ServiceDetail(
    val id: Int,
    @SerializedName("category_id")
    val categoryId: Int,
    val category: Category,
    val name: String,
    val phone: String,
    val address: String,
    val latitude: String?,
    val longitude: String?,
    @SerializedName("google_maps_url")
    val googleMapsUrl: String?,
    val description: String,
    @SerializedName("image_url")
    val image: String,
    val status: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

