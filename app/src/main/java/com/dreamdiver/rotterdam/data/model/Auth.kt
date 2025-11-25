package com.dreamdiver.rotterdam.data.model

import com.google.gson.annotations.SerializedName

// Registration Request
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    @SerializedName("password_confirmation")
    val passwordConfirmation: String,
    @SerializedName("user_type")
    val userType: String, // "general" or "worker"
    val phone: String,
    val city: String?,
    @SerializedName("skill_category")
    val skillCategory: String? = null,
    @SerializedName("hourly_rate")
    val hourlyRate: Double? = null
)

// Login Request
data class LoginRequest(
    val email: String,
    val password: String
)

// Auth Response
data class AuthResponse(
    val success: Boolean,
    val message: String,
    val data: AuthData?
)

data class AuthData(
    val user: User?,
    val token: String
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    @SerializedName("user_type")
    val userType: String, // "admin", "general", "worker"
    val phone: String?,
    val avatar: String?,
    val address: String?,
    val city: String?,
    val state: String?,
    @SerializedName("zip_code")
    val zipCode: String?,
    val latitude: Double?,
    val longitude: Double?,
    val status: String?,
    val profile: WorkerProfile?,
    @SerializedName("skill_category")
    val skillCategory: String?,
    @SerializedName("hourly_rate")
    val hourlyRate: Double?,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: String?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class WorkerProfile(
    val id: Int,
    @SerializedName("skill_category")
    val skillCategory: String?,
    val skills: List<String>?,
    val bio: String?,
    @SerializedName("hourly_rate")
    val hourlyRate: Double?,
    @SerializedName("experience_years")
    val experienceYears: Int?,
    val certifications: List<String>?,
    @SerializedName("is_verified")
    val isVerified: Boolean?,
    val availability: Map<String, String>?
)

// Profile Update Response
data class ProfileUpdateResponse(
    val success: Boolean,
    val message: String?,
    val data: ProfileData?,
    val errors: Map<String, List<String>>?
)

data class ProfileData(
    val user: User?,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("average_rating")
    val averageRating: Double?,
    @SerializedName("total_ratings")
    val totalRatings: Int?
)

