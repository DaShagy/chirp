package com.jjasystems.chirp.core.data.di

import com.jjasystems.chirp.core.data.auth.DataStoreSessionStorage
import com.jjasystems.chirp.core.data.auth.KtorAuthService
import com.jjasystems.chirp.core.data.logging.KermitLogger
import com.jjasystems.chirp.core.data.network.HttpClientFactory
import com.jjasystems.chirp.core.domain.auth.AuthService
import com.jjasystems.chirp.core.domain.auth.SessionStorage
import com.jjasystems.chirp.core.domain.logging.ChirpLogger
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformCoreDataModule: Module

val coreDataModule = module {
    includes(platformCoreDataModule)
    single<ChirpLogger> { KermitLogger }
    single {
        HttpClientFactory(get(), get()).create(get())
    }

    singleOf(::KtorAuthService) bind AuthService::class
    singleOf(::DataStoreSessionStorage) bind SessionStorage::class
}