package com.jjasystems.chirp.chat.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatParticipantEntity(
    @PrimaryKey
    val userId: String,
    val username: String,
    val profilePictureUrl: String?
)
