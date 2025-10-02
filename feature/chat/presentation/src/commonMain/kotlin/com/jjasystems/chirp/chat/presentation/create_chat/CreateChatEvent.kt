package com.jjasystems.chirp.chat.presentation.create_chat

import com.jjasystems.chirp.chat.domain.model.Chat

interface CreateChatEvent {
    data class OnChatCreated(val chat: Chat): CreateChatEvent
}