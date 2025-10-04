package com.jjasystems.chirp.chat.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatEntity(
    @PrimaryKey
    val chatId: String,
    val lastActivityAt: Long
)
