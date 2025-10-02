package com.jjasystems.chirp.chat.data.mapper

import com.jjasystems.chirp.chat.data.dto.ChatParticipantSerializable
import com.jjasystems.chirp.chat.domain.model.ChatParticipant

fun ChatParticipantSerializable.toDomain(): ChatParticipant {
    return ChatParticipant(
        username = username,
        userId = userId,
        profilePictureUrl = profilePictureUrl
    )
}