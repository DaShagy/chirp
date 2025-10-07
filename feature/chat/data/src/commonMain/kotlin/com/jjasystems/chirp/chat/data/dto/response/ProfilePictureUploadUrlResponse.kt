package com.jjasystems.chirp.chat.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ProfilePictureUploadUrlResponse(
    val uploadUrl: String,
    val publicUrl: String,
    val headers: Map<String, String>
)
