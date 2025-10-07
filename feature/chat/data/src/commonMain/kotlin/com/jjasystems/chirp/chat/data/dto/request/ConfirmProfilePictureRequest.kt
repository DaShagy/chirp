package com.jjasystems.chirp.chat.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmProfilePictureRequest(
    val publicUrl: String
)
