package com.jjasystems.chirp.chat.data.dto.websocket

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketMessageDto(
    val type: String,
    val payload: String
)
