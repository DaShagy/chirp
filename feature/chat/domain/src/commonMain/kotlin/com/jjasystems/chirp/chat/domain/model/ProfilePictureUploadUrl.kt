package com.jjasystems.chirp.chat.domain.model

data class ProfilePictureUploadUrl(
    val uploadUrl: String,
    val publicUrl: String,
    val headers: Map<String, String>
)
