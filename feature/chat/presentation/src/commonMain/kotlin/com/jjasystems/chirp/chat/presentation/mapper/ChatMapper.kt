package com.jjasystems.chirp.chat.presentation.mapper

import com.jjasystems.chirp.chat.domain.model.Chat
import com.jjasystems.chirp.chat.presentation.model.ChatUiModel

fun Chat.toUiModel(localParticipantId: String): ChatUiModel {
    val (local, other) = participants.partition { it.userId == localParticipantId }
    return ChatUiModel(
        id = id,
        localParticipant = local.first().toUiModel(),
        otherParticipants = other.map { it.toUiModel() },
        lastMessage = lastMessage,
        lastMessageSenderUsername = lastMessageSenderUsername
    )
}