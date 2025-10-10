package com.jjasystems.chirp.chat.data.di

import com.jjasystems.chirp.chat.data.lifecycle.AppLifecycleObserver
import com.jjasystems.chirp.chat.data.network.ConnectionErrorHandler
import com.jjasystems.chirp.chat.data.network.ConnectivityObserver
import com.jjasystems.chirp.chat.data.notification.FirebasePushNotificationService
import com.jjasystems.chirp.chat.database.DatabaseFactory
import com.jjasystems.chirp.chat.domain.notification.PushNotificationService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformChatDataModule: Module = module {
    singleOf(::DatabaseFactory)

    singleOf(::AppLifecycleObserver)
    singleOf(::ConnectivityObserver)
    singleOf(::ConnectionErrorHandler)

    singleOf(::FirebasePushNotificationService) bind PushNotificationService::class
}