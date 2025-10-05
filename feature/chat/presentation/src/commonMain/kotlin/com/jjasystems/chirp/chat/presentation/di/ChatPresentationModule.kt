package com.jjasystems.chirp.chat.presentation.di

import com.jjasystems.chirp.chat.presentation.chat_detail.ChatDetailViewModel
import com.jjasystems.chirp.chat.presentation.chat_list.ChatListViewModel
import com.jjasystems.chirp.chat.presentation.chat_list_detail.ChatListDetailViewModel
import com.jjasystems.chirp.chat.presentation.create_chat.CreateChatViewModel
import com.jjasystems.chirp.chat.presentation.manage_chat.ManageChatViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val chatPresentationModule = module {
    viewModelOf(::ChatListViewModel)
    viewModelOf(::ChatListDetailViewModel)
    viewModelOf(::CreateChatViewModel)
    viewModelOf(::ChatDetailViewModel)
    viewModelOf(::ManageChatViewModel)
}