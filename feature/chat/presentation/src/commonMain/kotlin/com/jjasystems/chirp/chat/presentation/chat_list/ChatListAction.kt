package com.jjasystems.chirp.chat.presentation.chat_list

sealed interface ChatListAction {
    data object OnUserAvatarClick: ChatListAction
    data object OnDismissUserMenu: ChatListAction
    data object OnLogoutClick: ChatListAction
    data object OnConfirmLogoutClick: ChatListAction
    data object OnDismissLogoutDialog: ChatListAction
    data object OnCreateChatClick: ChatListAction
    data object OnProfileSettings: ChatListAction
    data class OnSelectChat(val chatId: String?): ChatListAction
}