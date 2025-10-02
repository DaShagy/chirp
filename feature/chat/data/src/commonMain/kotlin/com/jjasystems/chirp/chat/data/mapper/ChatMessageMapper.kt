package com.jjasystems.chirp.chat.data.mapper

import com.jjasystems.chirp.chat.data.dto.ChatMessageSerializable
import com.jjasystems.chirp.chat.domain.model.ChatMessage
import kotlin.time.Instant

fun ChatMessageSerializable.toDomain(): ChatMessage {
    return ChatMessage(
        id = id,
        chatId = chatId,
        content = content,
        createdAt = Instant.parse(createdAt),
        senderId = senderId
    )
}