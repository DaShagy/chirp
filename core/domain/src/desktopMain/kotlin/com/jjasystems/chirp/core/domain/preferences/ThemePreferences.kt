package com.jjasystems.chirp.core.domain.preferences;

import kotlinx.coroutines.flow.Flow

interface ThemePreferences {
    fun observeThemePreference(): Flow<ThemePreference>
    suspend fun updateThemePreference(theme: ThemePreference)
}
