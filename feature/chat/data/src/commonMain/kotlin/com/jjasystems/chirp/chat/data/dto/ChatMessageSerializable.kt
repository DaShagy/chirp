package com.jjasystems.chirp.chat.data.dto

import com.jjasystems.chirp.chat.domain.model.ChatMessageDeliveryStatus
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageSerializable(
    val id: String,
    val chatId: String,
    val content: String,
    val createdAt: String,
    val senderId: String,
    val deliveryStatus: ChatMessageDeliveryStatus
)
