package com.jjasystems.chirp.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jjasystems.chirp.core.domain.preferences.ThemePreference
import com.jthemedetecor.OsThemeDetector
import java.util.function.Consumer

enum class AppTheme {
    LIGHT,
    DARK
}

@Composable
fun rememberAppTheme(
    themePreferenceFromAppSetting: ThemePreference
): AppTheme {
    var isSystemThemeDark by remember {
        if(OsThemeDetector.isSupported()) {
            mutableStateOf(OsThemeDetector.getDetector().isDark)
        } else {
            val isSettingsPreferenceDark = themePreferenceFromAppSetting == ThemePreference.DARK
            mutableStateOf(isSettingsPreferenceDark) }
    }

    DisposableEffect(Unit) {
        var listener: Consumer<Boolean>? = null

        if (OsThemeDetector.isSupported()) {
            listener = Consumer<Boolean> { dark -> isSystemThemeDark = dark }
            OsThemeDetector.getDetector().registerListener(listener)
        }

        onDispose {
            OsThemeDetector.getDetector().removeListener(listener)
        }
    }

    val isDarkTheme = when(themePreferenceFromAppSetting) {
        ThemePreference.LIGHT -> false
        ThemePreference.DARK -> true
        ThemePreference.SYSTEM -> isSystemThemeDark
    }

    return if(isDarkTheme) AppTheme.DARK else AppTheme.LIGHT
}