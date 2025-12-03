package com.dreamdiver.rotterdam.data.model

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    val success: Boolean,
    val data: List<Category>,
    val message: String,
    val locale: String? = null
)

data class Category(
    val id: Int,
    val name: String,
    @SerializedName("name_en")
    val nameEn: String? = null,
    @SerializedName("icon_url")
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
    val message: String,
    val locale: String? = null
)

data class Slider(
    val id: Int,
    val title: String,
    @SerializedName("title_en")
    val titleEn: String? = null,
    @SerializedName("short_details")
    val shortDetails: String,
    @SerializedName("short_details_en")
    val shortDetailsEn: String? = null,
    val details: String,
    @SerializedName("details_en")
    val detailsEn: String? = null,
    val image: String,
    val order: Int,
    val status: String,
    val locale: String? = null,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class ServiceResponse(
    val success: Boolean,
    val data: List<Service>,
    val count: Int,
    val message: String,
    val locale: String? = null
)

data class Service(
    val id: Int,
    @SerializedName("category_id")
    val categoryId: Int,
    val category: Category,
    val name: String,
    @SerializedName("name_en")
    val nameEn: String? = null,
    val phone: String,
    val address: String,
    @SerializedName("address_en")
    val addressEn: String? = null,
    val latitude: String?,
    val longitude: String?,
    @SerializedName("google_maps_url")
    val googleMapsUrl: String?,
    val description: String,
    @SerializedName("description_en")
    val descriptionEn: String? = null,
    @SerializedName("image_url")
    val image: String,
    val status: String,
    val locale: String? = null,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class ServiceDetailResponse(
    val success: Boolean,
    val data: ServiceDetail,
    val message: String,
    val locale: String? = null
)

data class ServiceDetail(
    val id: Int,
    @SerializedName("category_id")
    val categoryId: Int,
    val category: Category,
    val name: String,
    @SerializedName("name_en")
    val nameEn: String? = null,
    val phone: String,
    val address: String,
    @SerializedName("address_en")
    val addressEn: String? = null,
    val latitude: String?,
    val longitude: String?,
    @SerializedName("google_maps_url")
    val googleMapsUrl: String?,
    val description: String,
    @SerializedName("description_en")
    val descriptionEn: String? = null,
    @SerializedName("image_url")
    val image: String,
    val status: String,
    val locale: String? = null,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

// Featured Services Response
data class FeaturedServicesResponse(
    val success: Boolean,
    val data: List<Service>,
    val count: Int,
    val message: String
)

// Trending Response
data class TrendingResponse(
    val success: Boolean,
    val data: List<TrendingItem>,
    val count: Int,
    val message: String
)

data class TrendingItem(
    val id: Int,
    val title: String,
    @SerializedName("title_en")
    val titleEn: String? = null,
    val summary: String,
    @SerializedName("summary_en")
    val summaryEn: String? = null,
    val details: String,
    @SerializedName("details_en")
    val detailsEn: String? = null,
    val url: String?,
    val image: String,
    val status: String,
    val order: Int,
    val locale: String? = null,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)
