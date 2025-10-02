package com.jjasystems.chirp.chat.domain.model

data class ChatInfo(
    val chat: Chat,
    val messages: List<MessageWithSender>
)
