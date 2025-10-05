package com.jjasystems.chirp.chat.data.mapper

import com.jjasystems.chirp.chat.data.dto.ChatParticipantSerializable
import com.jjasystems.chirp.chat.database.entity.ChatParticipantEntity
import com.jjasystems.chirp.chat.domain.model.ChatParticipant

fun ChatParticipantSerializable.toDomain(): ChatParticipant {
    return ChatParticipant(
        username = username,
        userId = userId,
        profilePictureUrl = profilePictureUrl
    )
}

fun ChatParticipantEntity.toDomain(): ChatParticipant {
    return ChatParticipant(
        username = username,
        userId = userId,
        profilePictureUrl = profilePictureUrl
    )
}

fun ChatParticipant.toEntity(): ChatParticipantEntity {
    return ChatParticipantEntity(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl
    )
}