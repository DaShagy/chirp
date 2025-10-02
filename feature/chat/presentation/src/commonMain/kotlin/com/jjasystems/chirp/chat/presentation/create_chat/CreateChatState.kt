package com.jjasystems.chirp.chat.presentation.create_chat

import androidx.compose.foundation.text.input.TextFieldState
import com.jjasystems.chirp.core.design_system.components.avatar.ChatParticipantUi
import com.jjasystems.chirp.core.presentation.util.UiText

data class CreateChatState(
    val queryTextState: TextFieldState = TextFieldState(),
    val selectedChatParticipants: List<ChatParticipantUi> = emptyList(),
    val isSearching: Boolean = false,
    val canAddParticipant: Boolean = false,
    val currentSearchResult: ChatParticipantUi? = null,
    val searchError: UiText? = null,
    val isCreatingChat: Boolean = false,
    val createChatError: UiText? = null
)
