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
        val IS_ENGLISH_KEY = booleanPreferencesKey("is_english")
        val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
        val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
        val USER_ID_KEY = intPreferencesKey("user_id")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        val USER_TYPE_KEY = stringPreferencesKey("user_type")
        val USER_PHONE_KEY = stringPreferencesKey("user_phone")
        val USER_CITY_KEY = stringPreferencesKey("user_city")
        val USER_AVATAR_KEY = stringPreferencesKey("user_avatar")
    }

    val isEnglish: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_ENGLISH_KEY] ?: true // Default to English
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

    suspend fun setLanguage(isEnglish: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_ENGLISH_KEY] = isEnglish
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
        userAvatar: String?
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
        }
    }
}

