package com.jjasystems.chirp.chat.data.mapper

import com.jjasystems.chirp.chat.data.dto.ChatMessageSerializable
import com.jjasystems.chirp.chat.data.dto.websocket.IncomingWebSocketDto
import com.jjasystems.chirp.chat.data.dto.websocket.OutgoingWebSocketDto
import com.jjasystems.chirp.chat.database.entity.ChatMessageEntity
import com.jjasystems.chirp.chat.database.entity.MessageWithSenderEntity
import com.jjasystems.chirp.chat.database.view.LastMessageView
import com.jjasystems.chirp.chat.domain.model.ChatMessage
import com.jjasystems.chirp.chat.domain.model.ChatMessageDeliveryStatus
import com.jjasystems.chirp.chat.domain.model.MessageWithSender
import com.jjasystems.chirp.chat.domain.model.OutgoingNewMessage
import kotlin.time.Clock
import kotlin.time.Instant

fun ChatMessageSerializable.toDomain(): ChatMessage {
    return ChatMessage(
        id = id,
        chatId = chatId,
        content = content,
        createdAt = Instant.parse(createdAt),
        senderId = senderId,
        deliveryStatus = ChatMessageDeliveryStatus.SENT
    )
}

fun LastMessageView.toDomain(): ChatMessage {
    return ChatMessage(
        id = messageId,
        chatId = chatId,
        content = content,
        createdAt = Instant.fromEpochMilliseconds(timestamp),
        senderId = senderId,
        deliveryStatus = ChatMessageDeliveryStatus.valueOf(deliveryStatus)
    )
}


fun ChatMessage.toEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        messageId = id,
        chatId = chatId,
        senderId = senderId,
        content = content,
        timestamp = createdAt.toEpochMilliseconds(),
        deliveryStatus = deliveryStatus.toString()
    )
}

fun ChatMessage.toLastMessageView(): LastMessageView {
    return LastMessageView(
        messageId = id,
        chatId = chatId,
        senderId = senderId,
        content = content,
        timestamp = createdAt.toEpochMilliseconds(),
        deliveryStatus = deliveryStatus.name
    )
}

fun ChatMessageEntity.toDomain(): ChatMessage {
    return ChatMessage(
        id = messageId,
        chatId = chatId,
        content = content,
        createdAt = Instant.fromEpochMilliseconds(timestamp),
        senderId = senderId,
        deliveryStatus = ChatMessageDeliveryStatus.SENT
    )
}

fun MessageWithSenderEntity.toDomain(): MessageWithSender {
    return MessageWithSender(
        message = message.toDomain(),
        sender = sender.toDomain(),
        deliveryStatus = ChatMessageDeliveryStatus.valueOf(message.deliveryStatus)
    )
}

fun ChatMessage.toNewMessage(): OutgoingWebSocketDto.NewMessage {
    return OutgoingWebSocketDto.NewMessage(
        messageId = id,
        chatId = chatId,
        content = content
    )
}

fun IncomingWebSocketDto.NewMessageDto.toEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        messageId = id,
        chatId = chatId,
        senderId = senderId,
        content = content,
        timestamp = createdAt,
        deliveryStatus = ChatMessageDeliveryStatus.SENT.name
    )
}

fun OutgoingNewMessage.toWebSocketDto(): OutgoingWebSocketDto.NewMessage {
    return OutgoingWebSocketDto.NewMessage(
        chatId = chatId,
        messageId = messageId,
        content = content
    )
}

fun OutgoingWebSocketDto.NewMessage.toEntity(
    senderId: String,
    deliveryStatus: ChatMessageDeliveryStatus
): ChatMessageEntity {
    return ChatMessageEntity(
        messageId = messageId,
        chatId = chatId,
        senderId = senderId,
        content = content,
        timestamp = Clock.System.now().toEpochMilliseconds(),
        deliveryStatus = deliveryStatus.name
    )
}