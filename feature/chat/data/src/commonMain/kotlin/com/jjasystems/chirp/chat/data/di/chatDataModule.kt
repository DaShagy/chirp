package com.jjasystems.chirp.chat.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.jjasystems.chirp.chat.data.chat.KtorChatParticipantService
import com.jjasystems.chirp.chat.data.chat.KtorChatService
import com.jjasystems.chirp.chat.data.chat.OfflineFirstChatRepository
import com.jjasystems.chirp.chat.data.chat.WebSocketChatConnectionClient
import com.jjasystems.chirp.chat.data.message.OfflineFirstMessageRepository
import com.jjasystems.chirp.chat.data.network.KtorWebSocketConnector
import com.jjasystems.chirp.chat.database.DatabaseFactory
import com.jjasystems.chirp.chat.domain.chat.ChatConnectionClient
import com.jjasystems.chirp.chat.domain.chat.ChatParticipantService
import com.jjasystems.chirp.chat.domain.chat.ChatRepository
import com.jjasystems.chirp.chat.domain.chat.ChatService
import com.jjasystems.chirp.chat.domain.message.MessageRepository
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformChatDataModule: Module

val chatDataModule = module {
    includes(platformChatDataModule)

    singleOf(::KtorChatParticipantService) bind ChatParticipantService::class
    singleOf(::KtorChatService) bind ChatService::class
    singleOf(::OfflineFirstChatRepository) bind ChatRepository::class
    singleOf(::OfflineFirstMessageRepository) bind MessageRepository::class
    singleOf(::WebSocketChatConnectionClient) bind ChatConnectionClient::class
    singleOf(::KtorWebSocketConnector)

    single {
        Json {
            ignoreUnknownKeys = true
        }
    }


    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}