package com.jjasystems.chirp.chat.data.mapper

import com.jjasystems.chirp.chat.data.dto.ChatSerializable
import com.jjasystems.chirp.chat.domain.model.Chat
import kotlin.time.Instant

fun ChatSerializable.toDomain(): Chat {
    return Chat(
        id = id,
        participants = participants.map { it.toDomain() },
        lastActivityAt = Instant.parse(lastActivityAt),
        lastMessage = lastMessage?.toDomain()
    )
}