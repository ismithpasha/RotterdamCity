package com.dreamdiver.rotterdam.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {
    companion object {
        val IS_ENGLISH_KEY = booleanPreferencesKey("is_english")
    }

    val isEnglish: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_ENGLISH_KEY] ?: true // Default to English
    }

    suspend fun setLanguage(isEnglish: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_ENGLISH_KEY] = isEnglish
        }
    }
}

