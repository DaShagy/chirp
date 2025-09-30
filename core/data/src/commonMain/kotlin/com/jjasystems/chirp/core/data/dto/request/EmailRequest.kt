package com.jjasystems.chirp.core.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(
    val email: String
)
