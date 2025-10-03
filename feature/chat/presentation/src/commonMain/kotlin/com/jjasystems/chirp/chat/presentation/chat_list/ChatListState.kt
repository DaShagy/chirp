package com.jjasystems.chirp.chat.presentation.chat_list

import com.jjasystems.chirp.chat.presentation.model.ChatUi
import com.jjasystems.chirp.core.design_system.components.avatar.ChatParticipantUi
import com.jjasystems.chirp.core.presentation.util.UiText

data class ChatListState(
    val chats: List<ChatUi> = emptyList(),
    val error: UiText? = null,
    val localParticipant: ChatParticipantUi? = null,
    val isUserMenuOpen: Boolean = false,
    val showLogoutConfirmation: Boolean = false,
    val selectedChatId: String? = null,
    val isLoading: Boolean = false
)