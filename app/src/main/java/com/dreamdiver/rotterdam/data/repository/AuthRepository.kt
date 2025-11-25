package com.dreamdiver.rotterdam.data.repository

import com.dreamdiver.rotterdam.data.api.RetrofitInstance
import com.dreamdiver.rotterdam.data.model.AuthResponse
import com.dreamdiver.rotterdam.data.model.LoginRequest
import com.dreamdiver.rotterdam.data.model.ProfileUpdateResponse
import com.dreamdiver.rotterdam.data.model.RegisterRequest
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AuthRepository {
    private val api = RetrofitInstance.api

    suspend fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String,
        userType: String,
        phone: String,
        city: String?,
        skillCategory: String? = null,
        hourlyRate: Double? = null
    ): Result<AuthResponse> {
        return try {
            val request = RegisterRequest(
                name = name,
                email = email,
                password = password,
                passwordConfirmation = passwordConfirmation,
                userType = userType,
                phone = phone,
                city = city,
                skillCategory = skillCategory,
                hourlyRate = hourlyRate
            )
            val response = api.register(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val request = LoginRequest(email = email, password = password)
            val response = api.login(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProfile(token: String): Result<ProfileUpdateResponse> {
        return try {
            val response = api.getProfile("Bearer $token")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(
        token: String,
        name: String,
        phone: String?,
        address: String?,
        city: String?,
        state: String?,
        zipCode: String?,
        latitude: Double?,
        longitude: Double?,
        avatarFile: File?,
        // Worker-specific fields
        skillCategory: String?,
        skills: List<String>?,
        bio: String?,
        hourlyRate: Double?,
        experienceYears: Int?,
        certifications: List<String>?,
        availability: Map<String, String>?
    ): Result<ProfileUpdateResponse> {
        return try {
            val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val phonePart = phone?.toRequestBody("text/plain".toMediaTypeOrNull())
            val addressPart = address?.toRequestBody("text/plain".toMediaTypeOrNull())
            val cityPart = city?.toRequestBody("text/plain".toMediaTypeOrNull())
            val statePart = state?.toRequestBody("text/plain".toMediaTypeOrNull())
            val zipCodePart = zipCode?.toRequestBody("text/plain".toMediaTypeOrNull())
            val latitudePart = latitude?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
            val longitudePart = longitude?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())

            val skillCategoryPart = skillCategory?.toRequestBody("text/plain".toMediaTypeOrNull())

            // Convert skills list to List<RequestBody> for Laravel array format (skills[])
            val skillsParts = skills?.map { skill ->
                skill.toRequestBody("text/plain".toMediaTypeOrNull())
            }

            val bioPart = bio?.toRequestBody("text/plain".toMediaTypeOrNull())
            val hourlyRatePart = hourlyRate?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
            val experienceYearsPart = experienceYears?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())

            // Convert certifications list to List<RequestBody> for Laravel array format (certifications[])
            val certificationsParts = certifications?.map { cert ->
                cert.toRequestBody("text/plain".toMediaTypeOrNull())
            }

            // Availability as JSON string
            val availabilityPart = availability?.let {
                Gson().toJson(it).toRequestBody("text/plain".toMediaTypeOrNull())
            }

            val avatarPart = avatarFile?.let {
                val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("avatar", it.name, requestFile)
            }

            // Laravel method spoofing for PUT with multipart
            val methodPart = "PUT".toRequestBody("text/plain".toMediaTypeOrNull())

            val response = api.updateProfile(
                token = "Bearer $token",
                method = methodPart,
                name = namePart,
                phone = phonePart,
                address = addressPart,
                city = cityPart,
                state = statePart,
                zipCode = zipCodePart,
                latitude = latitudePart,
                longitude = longitudePart,
                skillCategory = skillCategoryPart,
                skills = skillsParts,
                bio = bioPart,
                hourlyRate = hourlyRatePart,
                experienceYears = experienceYearsPart,
                certifications = certificationsParts,
                availability = availabilityPart,
                avatar = avatarPart
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

