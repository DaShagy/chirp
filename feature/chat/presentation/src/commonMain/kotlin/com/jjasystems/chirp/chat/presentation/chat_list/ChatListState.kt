package com.jjasystems.chirp.chat.presentation.chat_list

import com.jjasystems.chirp.chat.presentation.model.ChatUiModel
import com.jjasystems.chirp.core.design_system.components.avatar.ChatParticipantUiModel
import com.jjasystems.chirp.core.presentation.util.UiText

data class ChatListState(
    val chats: List<ChatUiModel> = emptyList(),
    val error: UiText? = null,
    val localParticipant: ChatParticipantUiModel? = null,
    val isUserMenuOpen: Boolean = false,
    val showLogoutConfirmation: Boolean = false,
    val selectedChatId: String? = null,
    val isLoading: Boolean = false
)