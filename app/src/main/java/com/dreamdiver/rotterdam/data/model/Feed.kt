package com.dreamdiver.rotterdam.data.model

import com.google.gson.annotations.SerializedName

data class Feed(
    @SerializedName("id")
    val id: Int,

    @SerializedName("type")
    val type: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("title_en")
    val titleEn: String?,

    @SerializedName("title_nl")
    val titleNl: String?,

    @SerializedName("content")
    val content: String,

    @SerializedName("content_en")
    val contentEn: String?,

    @SerializedName("content_nl")
    val contentNl: String?,

    @SerializedName("image")
    val image: String?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("order")
    val order: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("published_at")
    val publishedAt: String?,

    @SerializedName("locale")
    val locale: String?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updatedAt: String?
)

data class FeedResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<Feed>,

    @SerializedName("type")
    val type: String?,

    @SerializedName("count")
    val count: Int,

    @SerializedName("message")
    val message: String
)

