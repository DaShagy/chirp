package com.jjasystems.chirp.chat.presentation.manage_chat

sealed interface ManageChatEvent {
    data object OnMembersAdded: ManageChatEvent
}