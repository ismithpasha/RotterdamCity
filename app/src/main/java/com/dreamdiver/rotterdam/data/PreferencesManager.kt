package com.dreamdiver.rotterdam.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {
    companion object {
        val LANGUAGE_KEY = stringPreferencesKey("app_language") // "en" or "nl"
        val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
        val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
        val USER_ID_KEY = intPreferencesKey("user_id")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        val USER_TYPE_KEY = stringPreferencesKey("user_type")
        val USER_PHONE_KEY = stringPreferencesKey("user_phone")
        val USER_CITY_KEY = stringPreferencesKey("user_city")
        val USER_AVATAR_KEY = stringPreferencesKey("user_avatar")
        val USER_ADDRESS_KEY = stringPreferencesKey("user_address")
        val USER_STATE_KEY = stringPreferencesKey("user_state")
        val USER_ZIP_CODE_KEY = stringPreferencesKey("user_zip_code")
        val USER_BIO_KEY = stringPreferencesKey("user_bio")
        val USER_SKILL_CATEGORY_KEY = stringPreferencesKey("user_skill_category")
        val USER_HOURLY_RATE_KEY = stringPreferencesKey("user_hourly_rate")
        val USER_EXPERIENCE_YEARS_KEY = intPreferencesKey("user_experience_years")
        val USER_SKILLS_KEY = stringPreferencesKey("user_skills")
        val USER_CERTIFICATIONS_KEY = stringPreferencesKey("user_certifications")
        val USER_AVERAGE_RATING_KEY = stringPreferencesKey("user_average_rating")
        val USER_TOTAL_RATINGS_KEY = intPreferencesKey("user_total_ratings")
    }

    val isEnglish: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY]?.equals("en") ?: true // Default to English
    }

    val language: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY] ?: "en" // Default to English
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY] ?: false
    }

    val authToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[AUTH_TOKEN_KEY]
    }

    val userId: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    val userName: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME_KEY]
    }

    val userEmail: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL_KEY]
    }

    val userType: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_TYPE_KEY]
    }

    val userPhone: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_PHONE_KEY]
    }

    val userCity: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_CITY_KEY]
    }

    val userAvatar: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_AVATAR_KEY]
    }

    val userAddress: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_ADDRESS_KEY]
    }

    val userState: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_STATE_KEY]
    }

    val userZipCode: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_ZIP_CODE_KEY]
    }

    val userBio: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_BIO_KEY]
    }

    val userSkillCategory: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_SKILL_CATEGORY_KEY]
    }

    val userHourlyRate: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_HOURLY_RATE_KEY]
    }

    val userExperienceYears: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[USER_EXPERIENCE_YEARS_KEY]
    }

    val userSkills: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_SKILLS_KEY]
    }

    val userCertifications: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_CERTIFICATIONS_KEY]
    }

    val userAverageRating: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_AVERAGE_RATING_KEY]
    }

    val userTotalRatings: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[USER_TOTAL_RATINGS_KEY]
    }

    suspend fun setLanguage(languageCode: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = languageCode
        }
    }

    suspend fun setLanguage(isEnglish: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = if (isEnglish) "en" else "nl"
        }
    }

    suspend fun saveUserData(
        token: String,
        userId: Int,
        userName: String,
        userEmail: String,
        userType: String,
        userPhone: String?,
        userCity: String?,
        userAvatar: String?,
        userAddress: String? = null,
        userState: String? = null,
        userZipCode: String? = null,
        userBio: String? = null,
        userSkillCategory: String? = null,
        userHourlyRate: Double? = null,
        userExperienceYears: Int? = null,
        userSkills: List<String>? = null,
        userCertifications: List<String>? = null,
        userAverageRating: Double? = null,
        userTotalRatings: Int? = null
    ) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = true
            preferences[AUTH_TOKEN_KEY] = token
            preferences[USER_ID_KEY] = userId
            preferences[USER_NAME_KEY] = userName
            preferences[USER_EMAIL_KEY] = userEmail
            preferences[USER_TYPE_KEY] = userType
            userPhone?.let { preferences[USER_PHONE_KEY] = it }
            userCity?.let { preferences[USER_CITY_KEY] = it }
            userAvatar?.let { preferences[USER_AVATAR_KEY] = it }
            userAddress?.let { preferences[USER_ADDRESS_KEY] = it }
            userState?.let { preferences[USER_STATE_KEY] = it }
            userZipCode?.let { preferences[USER_ZIP_CODE_KEY] = it }
            userBio?.let { preferences[USER_BIO_KEY] = it }
            userSkillCategory?.let { preferences[USER_SKILL_CATEGORY_KEY] = it }
            userHourlyRate?.let { preferences[USER_HOURLY_RATE_KEY] = it.toString() }
            userExperienceYears?.let { preferences[USER_EXPERIENCE_YEARS_KEY] = it }
            userSkills?.let { preferences[USER_SKILLS_KEY] = it.joinToString(",") }
            userCertifications?.let { preferences[USER_CERTIFICATIONS_KEY] = it.joinToString(",") }
            userAverageRating?.let { preferences[USER_AVERAGE_RATING_KEY] = it.toString() }
            userTotalRatings?.let { preferences[USER_TOTAL_RATINGS_KEY] = it }
        }
    }

    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.remove(IS_LOGGED_IN_KEY)
            preferences.remove(AUTH_TOKEN_KEY)
            preferences.remove(USER_ID_KEY)
            preferences.remove(USER_NAME_KEY)
            preferences.remove(USER_EMAIL_KEY)
            preferences.remove(USER_TYPE_KEY)
            preferences.remove(USER_PHONE_KEY)
            preferences.remove(USER_CITY_KEY)
            preferences.remove(USER_AVATAR_KEY)
            preferences.remove(USER_ADDRESS_KEY)
            preferences.remove(USER_STATE_KEY)
            preferences.remove(USER_ZIP_CODE_KEY)
            preferences.remove(USER_BIO_KEY)
            preferences.remove(USER_SKILL_CATEGORY_KEY)
            preferences.remove(USER_HOURLY_RATE_KEY)
            preferences.remove(USER_EXPERIENCE_YEARS_KEY)
            preferences.remove(USER_SKILLS_KEY)
            preferences.remove(USER_CERTIFICATIONS_KEY)
            preferences.remove(USER_AVERAGE_RATING_KEY)
            preferences.remove(USER_TOTAL_RATINGS_KEY)
        }
    }
}

