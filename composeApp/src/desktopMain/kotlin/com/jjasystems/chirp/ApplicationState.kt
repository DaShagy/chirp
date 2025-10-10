package com.jjasystems.chirp

import com.jjasystems.chirp.windows.WindowState

data class ApplicationState(
    val windows: List<WindowState> = listOf(WindowState())
)
