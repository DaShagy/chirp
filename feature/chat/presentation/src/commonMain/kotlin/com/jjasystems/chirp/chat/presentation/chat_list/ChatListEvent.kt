package com.jjasystems.chirp.chat.presentation.chat_list

import com.jjasystems.chirp.core.presentation.util.UiText

sealed interface ChatListEvent {
    data object OnLogoutSuccess: ChatListEvent
    data class OnLogoutError(val error: UiText): ChatListEvent
}