package com.jjasystems.chirp.di

import com.jjasystems.chirp.auth.presentation.di.authPresentationModule
import com.jjasystems.chirp.core.data.di.coreDataModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)

        modules(
            coreDataModule,
            authPresentationModule
        )
    }
}