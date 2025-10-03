package com.jjasystems.chirp.chat.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jjasystems.chirp.chat.presentation.chat_list_detail.ChatListAdaptiveLayout

fun NavGraphBuilder.chatGraph(
    navController: NavController
) {
    navigation<ChatGraphRoutes.Graph>(
        startDestination = ChatGraphRoutes.ChatListDetail
    ) {
        composable<ChatGraphRoutes.ChatListDetail> {
            ChatListAdaptiveLayout(
                onLogout = {
                    //TODO: Log out user
                }
            )
        }
    }
}