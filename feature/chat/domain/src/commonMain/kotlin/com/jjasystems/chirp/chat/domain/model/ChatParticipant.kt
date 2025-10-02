package com.jjasystems.chirp.chat.domain.model

data class ChatParticipant(
    val userId: String,
    val username: String,
    val profilePictureUrl: String?
) {
    val initials: String
        get() = username.take(2).uppercase()
}
