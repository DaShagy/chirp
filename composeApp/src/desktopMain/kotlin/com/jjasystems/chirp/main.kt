package com.jjasystems.chirp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.jjasystems.chirp.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Chirp"
        ) {
            App({})
        }
    }
}