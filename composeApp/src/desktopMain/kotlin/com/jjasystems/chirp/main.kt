package com.jjasystems.chirp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.application
import com.jjasystems.chirp.deeplink.DesktopDeeplinkHandler
import com.jjasystems.chirp.di.desktopModule
import com.jjasystems.chirp.di.initKoin
import com.jjasystems.chirp.navigation.ExternalUriHandler
import com.jjasystems.chirp.theme.rememberAppTheme
import com.jjasystems.chirp.windows.ChirpWindow
import org.koin.compose.koinInject

fun main(args: Array<String>) {
    initKoin {
        modules( desktopModule)
    }

    DesktopDeeplinkHandler.setup()

    val initialDeepLink = args.firstOrNull {
        val cleanedDeepLink = it.trim('"')

        DesktopDeeplinkHandler.supportedUriPatterns.any { it.matches(cleanedDeepLink) }
    }?.trim('"')

    application {
        val applicationStateHolder = koinInject<ApplicationStateHolder>()
        val applicationState by applicationStateHolder.state.collectAsState()
        val windows = applicationState.windows

        var canReceiveDeepLink by remember { mutableStateOf(false) }

        LaunchedEffect(canReceiveDeepLink) {
            if(canReceiveDeepLink && initialDeepLink != null) {
                ExternalUriHandler.onNewUri(initialDeepLink)
            }
        }

        LaunchedEffect(windows) {
            if (windows.isEmpty()) {
                exitApplication()
            }
        }

        val appTheme = rememberAppTheme(applicationState.themePreference)

        for (window in windows) {
            key(window.id) {
                ChirpWindow(
                    appTheme = appTheme,
                    onCloseRequest = {
                        applicationStateHolder.onWindowCloseRequest(window.id)
                    },
                    onFocusChanged = { isFocused ->
                        applicationStateHolder.onWindowFocusChanged(window.id, isFocused)
                    },
                    onAddWindowClick = {
                        applicationStateHolder.onAddWindowClick()
                    },
                    onDeepLinkListenerSetup = {
                        canReceiveDeepLink = true
                    }
                )
            }
        }

        ChirpTrayMenu(
            state = applicationState.trayState,
            themePreferenceFromAppSettings = applicationState.themePreference,
            onThemePreferenceClick = applicationStateHolder::onThemePreferenceClick
        )
    }
}