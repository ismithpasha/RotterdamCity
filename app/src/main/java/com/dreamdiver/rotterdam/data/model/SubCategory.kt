package com.dreamdiver.rotterdam.data.model

import com.google.gson.annotations.SerializedName

data class SubCategoryResponse(
    val success: Boolean,
    val data: List<SubCategory>
)

data class SubCategory(
    val id: Int,
    val name: String,
    @SerializedName("icon_url")
    val iconUrl: String?,
    @SerializedName("services_count")
    val servicesCount: Int
)

data class SubCategoryServiceResponse(
    val success: Boolean,
    val subcategory: SubCategoryInfo,
    val data: List<Service>
)

data class SubCategoryInfo(
    val id: Int,
    val name: String
)

