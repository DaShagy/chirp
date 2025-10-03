package com.jjasystems.chirp.chat.presentation.mapper

import com.jjasystems.chirp.chat.domain.model.ChatParticipant
import com.jjasystems.chirp.core.design_system.components.avatar.ChatParticipantUiModel

fun ChatParticipant.toUiModel() : ChatParticipantUiModel {
    return ChatParticipantUiModel(
        id = userId,
        username = username,
        initials = initials,
        imageUrl = profilePictureUrl
    )
}