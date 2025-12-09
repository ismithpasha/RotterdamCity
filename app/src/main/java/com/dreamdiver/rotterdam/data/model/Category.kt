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
    val name: String = "",
    @SerializedName("name_en")
    val nameEn: String? = null,
    @SerializedName(value = "icon", alternate = ["icon_url"])  // Handle both field names
    val icon: String = "",
    val status: String = "",
    @SerializedName("services_count")
    val servicesCount: Int? = null,  // Optional since nested categories may not have this
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
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
    val title: String? = null,
    @SerializedName("title_en")
    val titleEn: String? = null,
    @SerializedName("short_details")
    val shortDetails: String? = null,
    @SerializedName("short_details_en")
    val shortDetailsEn: String? = null,
    val details: String? = null,
    @SerializedName("details_en")
    val detailsEn: String? = null,
    val image: String? = null,
    val order: Int = 0,
    val status: String? = null,
    val locale: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null
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
    val category: Category? = null,
    val name: String? = null,
    @SerializedName("name_en")
    val nameEn: String? = null,
    val phone: String = "",
    val address: String = "",
    @SerializedName("address_en")
    val addressEn: String? = null,
    val latitude: String? = null,
    val longitude: String? = null,
    @SerializedName("google_maps_url")
    val googleMapsUrl: String? = null,
    val description: String = "",
    @SerializedName("description_en")
    val descriptionEn: String? = null,
    @SerializedName("image")
    val image: String? = null,
    val status: String = "",
    val locale: String? = null,
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
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
    val category: Category? = null,
    val name: String? = null,
    @SerializedName("name_en")
    val nameEn: String? = null,
    @SerializedName("name_nl")
    val nameNl: String? = null,
    val phone: String = "",
    val address: String = "",
    @SerializedName("address_en")
    val addressEn: String? = null,
    @SerializedName("address_nl")
    val addressNl: String? = null,
    val latitude: String? = null,
    val longitude: String? = null,
    @SerializedName("google_maps_url")
    val googleMapsUrl: String? = null,
    val description: String = "",
    @SerializedName("description_en")
    val descriptionEn: String? = null,
    @SerializedName("description_nl")
    val descriptionNl: String? = null,
    val descriptions: List<ServiceDescription> = emptyList(),
    @SerializedName("image")
    val image: String? = null,
    val url: String? = null,
    val status: String = "",
    val locale: String? = null,
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)

data class ServiceDescription(
    val id: Int,
    @SerializedName("tab_title")
    val tabTitle: String? = null,
    @SerializedName("tab_title_en")
    val tabTitleEn: String? = null,
    @SerializedName("tab_title_nl")
    val tabTitleNl: String? = null,
    val content: String? = null,
    @SerializedName("content_en")
    val contentEn: String? = null,
    @SerializedName("content_nl")
    val contentNl: String? = null,
    val order: Int = 0
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
    val title: String? = null,
    @SerializedName("title_en")
    val titleEn: String? = null,
    val summary: String? = null,
    @SerializedName("summary_en")
    val summaryEn: String? = null,
    val details: String? = null,
    @SerializedName("details_en")
    val detailsEn: String? = null,
    val url: String? = null,
    val image: String? = null,
    val status: String? = null,
    val order: Int = 0,
    val locale: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null
)
