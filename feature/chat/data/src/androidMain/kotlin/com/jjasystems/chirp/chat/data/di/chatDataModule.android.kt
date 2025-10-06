package com.jjasystems.chirp.chat.data.di

import com.jjasystems.chirp.chat.data.lifecycle.AppLifecycleObserver
import com.jjasystems.chirp.chat.data.network.ConnectionErrorHandler
import com.jjasystems.chirp.chat.data.network.ConnectivityObserver
import com.jjasystems.chirp.chat.database.DatabaseFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformChatDataModule = module {
    single { DatabaseFactory(androidContext()) }
    singleOf(::AppLifecycleObserver)
    singleOf(::ConnectivityObserver)
    singleOf(::ConnectionErrorHandler)
}