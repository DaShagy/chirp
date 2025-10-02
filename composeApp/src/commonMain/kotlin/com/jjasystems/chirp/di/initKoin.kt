package com.jjasystems.chirp.di

import com.jjasystems.chirp.auth.presentation.di.authPresentationModule
import com.jjasystems.chirp.chat.data.di.chatDataModule
import com.jjasystems.chirp.chat.presentation.di.chatPresentationModule
import com.jjasystems.chirp.core.data.di.coreDataModule
import com.jjasystems.chirp.core.presentation.di.corePresentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)

        modules(
            appModule,
            coreDataModule,
            corePresentationModule,
            authPresentationModule,
            chatPresentationModule,
            chatDataModule
        )
    }
}