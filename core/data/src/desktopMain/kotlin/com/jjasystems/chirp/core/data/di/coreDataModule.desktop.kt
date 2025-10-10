package com.jjasystems.chirp.core.data.di

import com.jjasystems.chirp.core.data.auth.createDataStore
import com.jjasystems.chirp.core.data.preferences.DataStoreThemePreferences
import com.jjasystems.chirp.core.domain.preferences.ThemePreferences
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformCoreDataModule: Module = module {
    single<HttpClientEngine> { OkHttp.create() }
    single { createDataStore() }
    singleOf(::DataStoreThemePreferences) bind ThemePreferences::class
}