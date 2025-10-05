package com.jjasystems.chirp.chat.presentation.components.manage_chat

import androidx.compose.foundation.text.input.TextFieldState
import com.jjasystems.chirp.core.design_system.components.avatar.ChatParticipantUiModel
import com.jjasystems.chirp.core.presentation.util.UiText

data class ManageChatState(
    val queryTextState: TextFieldState = TextFieldState(),
    val existingChatParticipants: List<ChatParticipantUiModel> = emptyList(),
    val selectedChatParticipants: List<ChatParticipantUiModel> = emptyList(),
    val isSearching: Boolean = false,
    val canAddParticipant: Boolean = false,
    val currentSearchResult: ChatParticipantUiModel? = null,
    val searchError: UiText? = null,
    val isCreatingChat: Boolean = false,
    val createChatError: UiText? = null
)
