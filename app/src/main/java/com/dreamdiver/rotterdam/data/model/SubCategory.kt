package com.dreamdiver.rotterdam.data.model

import com.google.gson.annotations.SerializedName

data class SubCategoryResponse(
    val success: Boolean,
    val data: List<SubCategory>,
    val locale: String? = null
)

data class CategoryTreeResponse(
    val success: Boolean,
    val data: CategoryTree,
    val locale: String? = null,
    val message: String? = null
)

data class CategoryTree(
    val id: Int,
    val name: String,
    @SerializedName("name_en")
    val nameEn: String? = null,
    val icon: String? = null,
    @SerializedName("icon_url")
    val iconUrl: String?,
    val status: String,
    val subcategories: List<SubCategory>
)

data class SubCategory(
    val id: Int,
    val name: String = "",
    @SerializedName("name_en")
    val nameEn: String? = null,
    val icon: String? = null,
    @SerializedName("icon_url")
    val iconUrl: String? = null,
    val status: String = "",
    val depth: Int = 0,
    val children: List<SubCategory> = emptyList(),
    @SerializedName("services_count")
    val servicesCount: Int? = null
)

data class SubCategoryServiceResponse(
    val success: Boolean,
    val subcategory: SubCategoryInfo,
    val data: List<Service>,
    val locale: String? = null
)

data class SubCategoryInfo(
    val id: Int,
    val name: String,
    @SerializedName("name_en")
    val nameEn: String? = null
)

