package com.jjasystems.chirp.chat.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.jjasystems.chirp.chat.presentation.chat_list_detail.ChatListAdaptiveLayout

fun NavGraphBuilder.chatGraph(
    navController: NavController
) {
    navigation<ChatGraphRoutes.Graph>(
        startDestination = ChatGraphRoutes.ChatListDetail(null)
    ) {
        composable<ChatGraphRoutes.ChatListDetail>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "chirp://chat_detail/{chatId}"
                }
            )
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<ChatGraphRoutes.ChatListDetail>()
            ChatListAdaptiveLayout(
                initialChatId = route.chatId,
                onLogout = {
                    //TODO: Log out user
                }
            )
        }
    }
}