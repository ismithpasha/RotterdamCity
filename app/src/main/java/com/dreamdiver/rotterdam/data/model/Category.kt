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

