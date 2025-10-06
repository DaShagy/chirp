package com.jjasystems.chirp.chat.presentation.chat_detail

import com.jjasystems.chirp.chat.presentation.model.ChatMessageUiModel

sealed interface ChatDetailAction {
    data object OnSendMessageClick: ChatDetailAction
    data object OnScrollToTop: ChatDetailAction
    data class OnSelectChat(val chatId: String?): ChatDetailAction
    data class OnDeleteMessageClick(val message: ChatMessageUiModel.LocalUserMessageUiModel): ChatDetailAction
    data class OnMessageLongClick(val message: ChatMessageUiModel.LocalUserMessageUiModel): ChatDetailAction
    data object OnDismissMessageMenu : ChatDetailAction
    data class OnRetryClick(val message: ChatMessageUiModel.LocalUserMessageUiModel): ChatDetailAction
    data object OnBackClick: ChatDetailAction
    data object OnChatOptionsClick: ChatDetailAction
    data object OnChatMembersClick: ChatDetailAction
    data object OnLeaveChatClick: ChatDetailAction
    data object OnDismissChatOptions: ChatDetailAction
    data object OnRetryPaginationClick : ChatDetailAction
}