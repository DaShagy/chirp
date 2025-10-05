package com.jjasystems.chirp.chat.presentation.chat_detail

import com.jjasystems.chirp.core.presentation.util.UiText

sealed interface ChatDetailEvent {
    data object OnChatLeft: ChatDetailEvent
    data class OnError(val error: UiText): ChatDetailEvent
}