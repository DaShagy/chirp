package com.jjasystems.chirp.core.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jjasystems.chirp.core.domain.preferences.ThemePreference
import com.jjasystems.chirp.core.domain.preferences.ThemePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreThemePreferences(
    private val dataStore: DataStore<Preferences>
): ThemePreferences {

    private val themePreferencesKey = stringPreferencesKey("theme_preference")

    override fun observeThemePreference(): Flow<ThemePreference> {
        return dataStore
            .data
            .map { preferences ->
                val currentPreference = preferences[themePreferencesKey] ?: ThemePreference.SYSTEM.name
                try {
                    ThemePreference.valueOf(currentPreference)
                } catch (e: Exception) {
                    ThemePreference.SYSTEM
                }
            }
    }

    override suspend fun updateThemePreference(theme: ThemePreference) {
        dataStore.edit { preferences ->
            preferences[themePreferencesKey] = theme.name
        }
    }
}