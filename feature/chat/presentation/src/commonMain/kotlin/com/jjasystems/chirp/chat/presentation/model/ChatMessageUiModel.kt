package com.jjasystems.chirp.chat.presentation.model

import com.jjasystems.chirp.chat.domain.model.ChatMessageDeliveryStatus
import com.jjasystems.chirp.core.design_system.components.avatar.ChatParticipantUiModel
import com.jjasystems.chirp.core.presentation.util.UiText

sealed interface ChatMessageUiModel {
    data class LocalUserMessageUiModel(
        val id: String,
        val content: String,
        val deliveryStatus: ChatMessageDeliveryStatus,
        val formattedSentTime: UiText,
        val isMenuOpen: Boolean
    ): ChatMessageUiModel

    data class OtherUserMessageUiModel(
        val id: String,
        val content: String,
        val formattedSentTime: UiText,
        val sender: ChatParticipantUiModel
    ): ChatMessageUiModel

    data class DateSeparatorUiModel(
        val id: String,
        val date: UiText
    ): ChatMessageUiModel
}
