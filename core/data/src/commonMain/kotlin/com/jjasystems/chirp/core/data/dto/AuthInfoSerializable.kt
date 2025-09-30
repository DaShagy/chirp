package com.jjasystems.chirp.core.data.dto

import com.jjasystems.chirp.core.data.dto.UserSerializable
import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoSerializable(
    val accessToken: String,
    val refreshToken: String,
    val user: UserSerializable
)