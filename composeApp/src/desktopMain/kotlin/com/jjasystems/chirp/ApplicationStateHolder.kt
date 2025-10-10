package com.jjasystems.chirp

import com.jjasystems.chirp.core.domain.preferences.ThemePreference
import com.jjasystems.chirp.core.domain.preferences.ThemePreferences
import com.jjasystems.chirp.windows.WindowState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ApplicationStateHolder(
    private val applicationScope: CoroutineScope,
    private val themePreferences: ThemePreferences
) {

    private val _state = MutableStateFlow(ApplicationState())
    val state = _state
        .onStart {
            observeThemePreferences()
        }
        .stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = _state.value
        )

    fun onAddWindowClick() {
        _state.update { it.copy(
            windows = it.windows + WindowState()
        ) }
    }

    fun onWindowCloseRequest(id: String) {
        _state.update { it.copy(
            windows = it.windows.filter { windowState -> windowState.id != id }
        )}
    }

    fun onThemePreferenceClick(themePreference: ThemePreference) {
        applicationScope.launch {
            themePreferences.updateThemePreference(themePreference)
        }
    }

    private fun observeThemePreferences() {
        themePreferences
            .observeThemePreference()
            .onEach { preference ->
                _state.update { it.copy(
                    themePreference = preference
                ) }
            }.launchIn(applicationScope)
    }
}