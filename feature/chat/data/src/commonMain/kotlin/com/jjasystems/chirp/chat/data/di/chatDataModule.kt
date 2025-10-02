package com.jjasystems.chirp.chat.data.di

import com.jjasystems.chirp.chat.data.chat.KtorChatParticipantService
import com.jjasystems.chirp.chat.data.chat.KtorChatService
import com.jjasystems.chirp.chat.domain.chat.ChatParticipantService
import com.jjasystems.chirp.chat.domain.chat.ChatService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val chatDataModule = module {
    singleOf(::KtorChatParticipantService) bind ChatParticipantService::class
    singleOf(::KtorChatService) bind ChatService::class
}