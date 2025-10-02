package com.jjasystems.chirp.core.design_system.components.avatar

data class ChatParticipantUi(
    val id: String,
    val username: String,
    val initials: String,
    val imageUrl: String? = null
)
