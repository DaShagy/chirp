package com.jjasystems.chirp.core.data.di

import com.jjasystems.chirp.core.data.auth.createDataStore
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformCoreDataModule: Module = module {
    single<HttpClientEngine> { OkHttp.create() }
    single { createDataStore() }
}