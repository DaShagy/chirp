package com.jjasystems.chirp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.jjasystems.chirp.auth.presentation.navigation.AuthGraphRoutes
import com.jjasystems.chirp.auth.presentation.navigation.authGraph
import com.jjasystems.chirp.chat.presentation.navigation.ChatGraphRoutes
import com.jjasystems.chirp.chat.presentation.navigation.chatGraph

@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: Any
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(
            navController = navController,
            onLoginSuccess = {
                navController.navigate(ChatGraphRoutes.Graph) {
                    popUpTo(AuthGraphRoutes.Graph) {
                        inclusive = true
                    }
                }
            }
        )

        chatGraph(
            navController = navController
        )
    }
}