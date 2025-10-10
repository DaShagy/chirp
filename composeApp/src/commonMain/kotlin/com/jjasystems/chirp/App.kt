package com.jjasystems.chirp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.jjasystems.chirp.auth.presentation.navigation.AuthGraphRoutes
import com.jjasystems.chirp.chat.presentation.navigation.ChatGraphRoutes
import org.jetbrains.compose.ui.tooling.preview.Preview

import com.jjasystems.chirp.core.design_system.theme.ChirpTheme
import com.jjasystems.chirp.core.presentation.util.ObserveAsEvents
import com.jjasystems.chirp.navigation.DeepLinkListener
import com.jjasystems.chirp.navigation.NavigationRoot
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    onAuthenticatedCheck: () -> Unit,
    viewModel: MainViewModel = koinViewModel()
) {
    val navController = rememberNavController()

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isCheckingAuth) {
        if(!state.isCheckingAuth) {
            onAuthenticatedCheck()
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            MainEvent.OnSessionExpired -> {
                navController.navigate(AuthGraphRoutes.Graph) {
                    popUpTo(AuthGraphRoutes.Graph) {
                        inclusive = false
                    }
                }
            }
        }
    }

    ChirpTheme(
        darkTheme = isDarkTheme
    ) {
        if(!state.isCheckingAuth) {
            NavigationRoot(
                navController = navController,
                startDestination = if(state.isLoggedIn) {
                    ChatGraphRoutes.Graph
                } else {
                    AuthGraphRoutes.Graph
                }
            )

            DeepLinkListener(navController)
        }
    }
}