package com.jjasystems.chirp.chat.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class MessageWithSender(
    @Embedded
    val message: ChatMessageEntity,
    @Relation(
        parentColumn = "senderId",
        entityColumn = "userId"
    )
    val sender: ChatParticipantEntity
)
