package com.jjasystems.chirp.core.presentation.di

import com.jjasystems.chirp.core.presentation.util.ScopedStoreRegistryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val corePresentationModule = module {
    viewModelOf(::ScopedStoreRegistryViewModel)
}