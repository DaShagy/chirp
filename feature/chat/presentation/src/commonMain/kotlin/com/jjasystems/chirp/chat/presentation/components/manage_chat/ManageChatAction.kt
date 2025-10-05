package com.jjasystems.chirp.chat.presentation.components.manage_chat

sealed interface ManageChatAction {
    data object OnAddClick: ManageChatAction
    data object OnDismissDialog: ManageChatAction
    data object OnPrimaryActionClick: ManageChatAction
}