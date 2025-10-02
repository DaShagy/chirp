package com.jjasystems.chirp.chat.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface ChatGraphRoutes {
    @Serializable
    data object Graph: ChatGraphRoutes

    @Serializable
    data object ChatListDetail: ChatGraphRoutes
}

