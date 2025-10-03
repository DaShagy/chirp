package com.jjasystems.chirp.chat.presentation.chat_detail

import androidx.compose.foundation.text.input.TextFieldState
import com.jjasystems.chirp.chat.domain.model.ConnectionState
import com.jjasystems.chirp.chat.presentation.model.ChatMessageUiModel
import com.jjasystems.chirp.chat.presentation.model.ChatUiModel
import com.jjasystems.chirp.core.presentation.util.UiText

data class ChatDetailState(
    val chatUi: ChatUiModel? = null,
    val isLoading: Boolean = false,
    val messages: List<ChatMessageUiModel> = listOf(),
    val error: UiText? = null,
    val messageTextFieldState: TextFieldState = TextFieldState(),
    val canSendMessage: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val paginationError: UiText? = null,
    val endReached: Boolean = false,
    val bannerState: BannerState = BannerState(),
    val isChatOptionsOpen: Boolean = false,
    val isNearBottom: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED
)

data class BannerState(
    val formattedDate: UiText? = null,
    val isVisible: Boolean = false
)