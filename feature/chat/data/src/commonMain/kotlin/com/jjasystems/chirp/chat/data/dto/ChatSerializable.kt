package com.jjasystems.chirp.chat.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatSerializable(
    val id: String,
    val participants: List<ChatParticipantSerializable>,
    val lastActivityAt: String,
    val lastMessage: ChatMessageSerializable?
)
