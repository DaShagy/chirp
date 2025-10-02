package com.jjasystems.chirp.chat.presentation.model

import com.jjasystems.chirp.chat.domain.model.ChatMessage
import com.jjasystems.chirp.core.design_system.components.avatar.ChatParticipantUi

data class ChatUi(
    val id: String,
    val localParticipant: ChatParticipantUi,
    val otherParticipants: List<ChatParticipantUi>,
    val lastMessage: ChatMessage?,
    val lastMessageSenderUsername: String?
)
