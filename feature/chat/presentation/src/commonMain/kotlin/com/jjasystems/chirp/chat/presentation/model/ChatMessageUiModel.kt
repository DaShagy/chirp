package com.jjasystems.chirp.chat.presentation.model

import com.jjasystems.chirp.chat.domain.model.ChatMessageDeliveryStatus
import com.jjasystems.chirp.core.design_system.components.avatar.ChatParticipantUiModel
import com.jjasystems.chirp.core.presentation.util.UiText

sealed class ChatMessageUiModel(open val id: String) {
    data class LocalUserMessageUiModel(
        override val id: String,
        val content: String,
        val deliveryStatus: ChatMessageDeliveryStatus,
        val formattedSentTime: UiText
    ): ChatMessageUiModel(id)

    data class OtherUserMessageUiModel(
        override val id: String,
        val content: String,
        val formattedSentTime: UiText,
        val sender: ChatParticipantUiModel
    ): ChatMessageUiModel(id)

    data class DateSeparatorUiModel(
        override val id: String,
        val date: UiText
    ): ChatMessageUiModel(id)
}
