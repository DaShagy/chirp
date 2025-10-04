package com.jjasystems.chirp.chat.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.jjasystems.chirp.chat.data.chat.KtorChatParticipantService
import com.jjasystems.chirp.chat.data.chat.KtorChatService
import com.jjasystems.chirp.chat.database.DatabaseFactory
import com.jjasystems.chirp.chat.domain.chat.ChatParticipantService
import com.jjasystems.chirp.chat.domain.chat.ChatService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect  val platformChatDataModule: Module

val chatDataModule = module {
    includes(platformChatDataModule)

    singleOf(::KtorChatParticipantService) bind ChatParticipantService::class
    singleOf(::KtorChatService) bind ChatService::class

    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}