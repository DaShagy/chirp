package com.jjasystems.chirp.chat.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatParticipantSerializable(
    val userId: String,
    val username: String,
    val profilePictureUrl: String?
)
