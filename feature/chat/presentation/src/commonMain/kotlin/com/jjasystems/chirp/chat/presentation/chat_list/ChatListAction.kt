package com.jjasystems.chirp.chat.presentation.chat_list

import com.jjasystems.chirp.chat.presentation.model.ChatUiModel

sealed interface ChatListAction {
    data object OnUserAvatarClick: ChatListAction
    data object OnDismissUserMenu: ChatListAction
    data object OnLogoutClick: ChatListAction
    data object OnConfirmLogoutClick: ChatListAction
    data object OnDismissLogoutDialog: ChatListAction
    data object OnCreateChatClick: ChatListAction
    data object OnProfileSettings: ChatListAction
    data class OnChatClick(val chat: ChatUiModel): ChatListAction
}