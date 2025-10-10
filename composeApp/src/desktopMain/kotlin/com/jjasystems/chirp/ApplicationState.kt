package com.jjasystems.chirp

import androidx.compose.ui.window.TrayState
import com.jjasystems.chirp.core.domain.preferences.ThemePreference
import com.jjasystems.chirp.windows.WindowState

data class ApplicationState(
    val windows: List<WindowState> = listOf(WindowState()),
    val themePreference: ThemePreference = ThemePreference.SYSTEM,
    val trayState: TrayState = TrayState()
)
