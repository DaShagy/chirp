package com.jjasystems.chirp.auth.presentation.di

import com.jjasystems.chirp.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(::RegisterViewModel)
}