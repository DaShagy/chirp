package com.jjasystems.chirp.chat.presentation.mapper

import com.jjasystems.chirp.chat.domain.model.ChatParticipant
import com.jjasystems.chirp.core.design_system.components.avatar.ChatParticipantUi

fun ChatParticipant.toUiModel() : ChatParticipantUi {
    return ChatParticipantUi(
        id = userId,
        username = username,
        initials = initials,
        imageUrl = profilePictureUrl
    )
}