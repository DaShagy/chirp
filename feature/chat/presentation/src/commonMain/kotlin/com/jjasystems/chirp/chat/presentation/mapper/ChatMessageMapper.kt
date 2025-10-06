package com.jjasystems.chirp.chat.presentation.mapper

import com.jjasystems.chirp.chat.domain.model.MessageWithSender
import com.jjasystems.chirp.chat.presentation.model.ChatMessageUiModel
import com.jjasystems.chirp.chat.presentation.util.DateUtils

fun List<MessageWithSender>.toUiModel(localUserId: String): List<ChatMessageUiModel> {
    return this
        .sortedByDescending { it.message.createdAt }
        .map { it.toUiModel(localUserId) }
}
fun MessageWithSender.toUiModel(
    localUserId: String
): ChatMessageUiModel {
    val isFromLocalUser = this.sender.userId == localUserId
    return if(isFromLocalUser) {
        ChatMessageUiModel.LocalUserMessageUiModel(
            id = message.id,
            content = message.content,
            deliveryStatus = message.deliveryStatus,
            formattedSentTime = DateUtils.formatMessageTime(instant = message.createdAt)
        )
    } else {
        ChatMessageUiModel.OtherUserMessageUiModel(
            id = message.id,
            content = message.content,
            formattedSentTime = DateUtils.formatMessageTime(instant = message.createdAt),
            sender = sender.toUiModel()
        )
    }
}