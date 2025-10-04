package com.jjasystems.chirp.chat.data.di

import com.jjasystems.chirp.chat.database.DatabaseFactory
import org.koin.dsl.module

actual val platformChatDataModule = module {
    single { DatabaseFactory() }
}