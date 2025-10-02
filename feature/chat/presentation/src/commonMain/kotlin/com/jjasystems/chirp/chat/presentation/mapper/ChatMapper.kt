package com.jjasystems.chirp.chat.presentation.mapper

import com.jjasystems.chirp.chat.domain.model.Chat
import com.jjasystems.chirp.chat.presentation.model.ChatUi

fun Chat.toUiModel(localParticipantId: String): ChatUi {
    val (local, other) = participants.partition { it.userId == localParticipantId }
    return ChatUi(
        id = id,
        localParticipant = local.first().toUiModel(),
        otherParticipants = other.map { it.toUiModel() },
        lastMessage = lastMessage,
        lastMessageSenderUsername = participants.find { it.userId == lastMessage?.senderId }?.username
    )
}