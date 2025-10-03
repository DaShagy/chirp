package com.jjasystems.chirp.chat.presentation.model

import com.jjasystems.chirp.chat.domain.model.ChatMessage
import com.jjasystems.chirp.core.design_system.components.avatar.ChatParticipantUiModel

data class ChatUiModel(
    val id: String,
    val localParticipant: ChatParticipantUiModel,
    val otherParticipants: List<ChatParticipantUiModel>,
    val lastMessage: ChatMessage?,
    val lastMessageSenderUsername: String?
)
