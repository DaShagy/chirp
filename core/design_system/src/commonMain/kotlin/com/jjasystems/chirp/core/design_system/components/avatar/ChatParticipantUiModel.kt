package com.jjasystems.chirp.core.design_system.components.avatar

data class ChatParticipantUiModel(
    val id: String,
    val username: String,
    val initials: String,
    val imageUrl: String? = null
)
